(ns rmc-core-2018-2019.network.network-core
   (:require [clojure.java.io :as io]
             [manifold.stream :as s])
   (:import (rmc_core_2018_2019.network NetworkConstants)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; bytes->num
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn bytes->num [data]
   (reduce bit-or
           (map-indexed
              (fn [i x]
                 (bit-shift-left
                    (bit-and x 0x0FF)
                    (* 8
                       (-
                          (count data)
                          i 1))))
              data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; handle-new-message
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- handle-new-message
   "Handles a new incoming message that was sent by the GUI."
   [bytes]

   {:pre [(seq? bytes)]}

   (let
      [command (first bytes)
       args    (rest bytes)]

      (case command
         ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
         ; case PRINT-AS-INTEGERS
         ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
         NetworkConstants/PRINT_AS_NUMBERS_MESSAGE_ID
         (println args)

         ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
         ; case PRINT-AS-STRINGS
         ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
         NetworkConstants/PRINT_AS_STRING_MESSAGE_ID
         (->
            args
            byte-array
            String.
            println)

         ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
         ; case LOG
         ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
         NetworkConstants/LOG_MESSAGE_ID
         (with-open
            [logger (io/writer
                       "log.log"
                       :append
                       true)]
            (.write logger
                    (str args "\n")))

         ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
         ; default
         ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
         (println "Received unknown message"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; make-connection-handler
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- make-connection-handler [messageHandler clientStreams]
   "Generates a handler function that will handle new aleph-style connections."
   {:pre  [(ifn? messageHandler)]
    :post [(ifn? %)]}
   (fn [stream _]
      {:post [;(contains? @clientStreams stream)
              ]}
      (do
         (dosync
            (alter clientStreams conj stream))
         (io!
            (s/consume-async
               (fn [bytes]
                  (-> bytes
                      byte-streams/to-byte-array
                      seq
                      messageHandler))
               stream)))))

(defn launch-tcp-server! []
   (let
      [clientStreams (ref [])
       handler       (make-connection-handler handle-new-message
                                              clientStreams)]
      (aleph.tcp/start-server handler {:port 2401})
      clientStreams))