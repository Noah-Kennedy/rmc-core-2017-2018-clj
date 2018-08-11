(ns rmc-core-2018-2019.tcp
   (:require [clojure.java.io :as io]
             [manifold.stream :as s]
             aleph.tcp
             [byte-streams]
             [rmc-core-2018-2019.common :refer :all]))

(defn log! [fileName message]
   (io! (with-open [logger (io/writer fileName :append true)]
           (.write logger (str message "\n")))))

(defn bytes->string [bytes]
   (io! (-> bytes
            (byte-array)
            (String.))))

(defn print-as-bytes! [bytes]
   (println bytes))

(defn print-as-string! [bytes]
   (io! (-> bytes
            (bytes->string)
            (println))))

(defn handle-new-message!
   "Handles a new incoming message that was sent by the GUI."
   [bytes]
   {:pre [(seq? bytes)]}
   (io!
      (let
         [command (first bytes)
          args    (rest bytes)]
         (case command
            PRINT-AS-BYTES-MESSAGE-ID (print-as-bytes! args)
            PRINT-AS-STRING-MESSAGE-ID (print-as-string! args)
            LOG-MESSAGE-ID (log! "netlog.log" bytes)
            ;default
            (println "Received unknown message")))))

(defn make-connection-handler [messageHandler clientStreams]
   (fn [stream _]
      (io! (dosync
              (alter clientStreams conj stream)))
      (io!
         (s/consume-async
            (fn [bytes]
               (-> bytes
                   byte-streams/to-byte-array
                   seq
                   messageHandler))
            stream))))

(defrecord TCP [server clientStreams])

(defn launch-server! []
   (let
      [clientStreams (ref [])
       handler       (make-connection-handler handle-new-message! clientStreams)
       server        (aleph.tcp/start-server handler {:port 2401})]
      (->TCP server clientStreams)))