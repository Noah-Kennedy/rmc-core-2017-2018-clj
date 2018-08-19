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

(defn tcp-server?
   "Tests x to see if x is an instance of the TCPServer type."
   [x]
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

(defn make-message-handler
   "Makes a new message handler function that uses the supplied
   `messageValidator` function to validate the message and then handles the message
   differently based on whether or not it was validated.

   `messageValidator`      := A function that validates the message. This
                              function should always return a boolean.

   `validMessageHandler`   := A function which handles valid messages. This
                              function will be called when a call to the
                              supplied `messageValidator` function returns true.

   `invalidMessageHandler` := A function which handles invalid messages. This
                              function will be called when a call to the
                              supplied messageValidator function returns false."
   [messageValidator
    validMessageHandler
    invalidMessageHandler]
   {:pre  [(fn? validMessageHandler)
           (fn? messageValidator)
           (fn? invalidMessageHandler)]
    :post [(fn? %)]}
   (fn [newMessage]
      (if (messageValidator newMessage)
         (validMessageHandler newMessage)
         (invalidMessageHandler newMessage))))