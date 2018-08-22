(ns rmc-core-2018-2019.arduino
   (:require [rmc-core-2018-2019.comms :refer :all]
             [rmc-core-2018-2019.common :refer :all]
             [clojure.core.async :as async])
   (:import (purejavacomm CommPortIdentifier)
            (java.io OutputStream)
            (java.util Scanner)))

(defrecord Arduino [reader writer]
   Transmitter
   (transmit! [this message]
      {:pre [(seqable? message)
             (every? byte? message)]}
      (->> message
           (byte-array)
          (.write writer)))
   Consumer
   (consume-async! [this handler]
      {:pre [(fn? handler)]}
      (async/go-loop []
         (when (.hasNext)
            (handler (.next reader))))))

(defn create-arduino [^String appName
                      ^String port
                      ^Number timeout]
   (let [commPort (-> (CommPortIdentifier/getPortIdentifier port)
                      (.open appName timeout))]
      (->Arduino (-> commPort
                     (.getInputStream)
                     (Scanner.))
                 (-> commPort
                     (.getOutputStream)
                     (agent)))))