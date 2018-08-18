(ns rmc-core-2018-2019.tcp
   (:require [manifold.stream :as s]
             aleph.tcp
             [byte-streams]
             [rmc-core-2018-2019.common :refer :all]
             [rmc-core-2018-2019.comms :refer :all]
             [clojure.core.specs.alpha :as spec]))

(defrecord TCPServer [tcpClientStreams connectionStatusAtom server]
   Transmitter
   Consumer)

(defn tcp-server? [x]
   {:post [(boolean? %)]}
   (instance? TCPServer x))

(defn create-tcp-server! [port]
   {:pre [(number? port)]}
   (let [clientStreams        (ref [])
         connectionStatusAtom (atom false)
         newConnectionHandler (fn [clientStream]
                                 (add-sync! clientStreams clientStream))
         server               (aleph.tcp/start-server newConnectionHandler
                                                      {:port port})]
      (->TCPServer clientStreams connectionStatusAtom server)))

(defn mk-message-handler [validMessageHandler]
   (fn [newMessage]
      ()))