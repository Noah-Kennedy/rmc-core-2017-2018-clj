(ns rmc-core-2018-2019.arduino
   (:require [rmc-core-2018-2019.comms :refer :all]
             [rmc-core-2018-2019.common :refer :all]
             [clojure.core.async :as async])
   (:import (purejavacomm CommPortIdentifier)
            (clojure.lang Agent)
            (java.io InputStream)))

(defrecord Arduino [^InputStream reader, ^Agent writer]
   Transmitter
   (transmit! [this message]
      {:pre  [(seqable? message)
              (every? byte? message)]
       :post [(not (agent-error writer))]}
      (send writer (fn [writer] (.write writer (byte-array message))
                      writer)))
   Receiver
   (has-new? [this]
      ;{:post [(boolean? %)]}
      (.available reader))
   (receive! [this]
      {:pre [(not (agent-error writer))
             (has-new? this)]
       ;:post [(bytes? %)]
       }
      (let [availableBytes (.available reader)]
         (if (not (zero? availableBytes))
            (let [bytes (byte-array availableBytes)]
               (.read reader bytes)
                bytes)
            nil))))

(defn create-arduino [^String appName, ^String port, ^Number timeout]
   (let [commPort (.open (CommPortIdentifier/getPortIdentifier port)
                         appName
                         timeout)
         arduino  (->Arduino (-> commPort (.getInputStream))
                             (-> commPort (.getOutputStream) (agent)))]
      arduino))