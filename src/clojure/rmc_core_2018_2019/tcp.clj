(ns rmc-core-2018-2019.tcp
   (:require [manifold.stream :as s]
             aleph.tcp
             [byte-streams]
             [rmc-core-2018-2019.common :refer :all]
             [rmc-core-2018-2019.comms :refer :all]
             [clojure.core.specs.alpha :as spec]))

(defrecord TCPServer [tcpClientStreams connectionStatusAtom server]
   Consumer
   (consume! [this consumerFn]
      {:pre [(fn? consume-async!)
             (ref? tcpClientStreams)]}
      (dosync (doseq
                 [client (ensure tcpClientStreams)]
                 (s/consume consumerFn client))))
   (consume-async! [this consumerFn]
      {:pre [(fn? consume-async!)
             (ref? tcpClientStreams)
             (seqable? (deref tcpClientStreams))]}
      (dosync (doseq
                 [client (ensure tcpClientStreams)]
                 (s/consume-async consumerFn client))))
   Transmitter
   (transmit! [this message]
      (dosync (doseq
                 [client (ensure tcpClientStreams)]
                 (s/put! client message)))))

(defn tcp-server?
   "Tests x to see if x is an instance of the TCPServer type."
   [x]
   {:post [(boolean? %)]}
   (instance? TCPServer x))

(defn create-tcp-server! [port]
   {:pre  [(number? port)]
    :post [(tcp-server? %)]}
   (let [clientStreams        (ref [])
         connectionStatusAtom (atom false)
         newConnectionHandler (fn [clientStream]
                                 (add-sync! clientStreams clientStream))
         server               (aleph.tcp/start-server newConnectionHandler
                                                      {:port port})]
      (->TCPServer clientStreams connectionStatusAtom server)))