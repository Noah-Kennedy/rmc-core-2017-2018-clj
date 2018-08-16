(ns rmc-core-2018-2019.tcp
   (:require [manifold.stream :as s]
             aleph.tcp
             [byte-streams]
             [rmc-core-2018-2019.common :refer :all]
             [clojure.core.specs.alpha :as spec]))

(def clientStreams (ref []))

(defprotocol Transmitter
   (send! [this message]))

(defrecord TCP [server clientStreams]
   Transmitter
   (send! [this message]
      {:pre [(or (bytes? message)
                 (string? message))
             (ref? clientStreams)]}
      (dorun (pmap
                (fn [stream]
                   (s/put! stream message))
                (deref clientStreams)))))

(defn tcp? [x]
   (instance? TCP x))

(defn- byte-stream->seq [bytes]
   {:post [(seq? %)]}
   (-> bytes
       (byte-streams/to-byte-array)
       (seq)))

(defn bytes->string [nums]
   {:pre  [(sequential? nums)
           (every? number? nums)]
    :post [(string? %)]}
   (-> nums
       (byte-array)
       (String.)))

(defn print-as-nums! [nums]
   {:pre [(sequential? nums)
          (every? number? nums)]}
   (io! (println nums)))

(defn print-as-string! [bytes]
   (io! (-> bytes
            (bytes->string)
            (println))))

(defn send-echo-response! [tcpServer payload]
   {:pre [(tcp? tcpServer)
          (sequential? payload)
          (every? byte? payload)]}
   (send! tcpServer (byte-array (cons ECHO-RESPONSE-MESSAGE-ID payload))))

(defn handle-new-message!
   "Handles a new incoming message that was sent by the GUI."
   [bytes]
   {:pre [(sequential? bytes)
          (every? byte? bytes)]}
   (let
      [[command & args] bytes]
      (condp = command
         PRINT-AS-BYTES-MESSAGE-ID (print-as-nums! args)
         PRINT-AS-STRING-MESSAGE-ID (print-as-string! args)
         LOG-MESSAGE-ID (log! "netlog.log" bytes)
         ECHO-MESSAGE-ID (send-echo-response! clientStreams args)
         :else (do (print "Received unknown message: ")
                   (print-as-nums! bytes)))))

(defn make-connection-handler [messageHandler clientStreams]
   {:pre  [(ifn? messageHandler)
           (ref? clientStreams)
           (sequential? (deref clientStreams))]
    :post [(ifn? %)]}
   (fn [stream _]
      ;{:post [(contains? (deref clientStreams) stream)]}
      (add-sync! clientStreams stream)
      (s/consume-async
         (comp messageHandler byte-stream->seq)
         stream)))

(defn launch-server! []
   (let
      [handler       (make-connection-handler handle-new-message! clientStreams)
       server        (aleph.tcp/start-server handler {:port 2401})]
      (->TCP server clientStreams)))