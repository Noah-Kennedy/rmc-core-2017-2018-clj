(ns rmc-core-2018-2019.handlers
   (:require [rmc-core-2018-2019.common :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Message IDs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ^:const ^Byte PRINT-AS-BYTES-MESSAGE-ID 16)
(def ^:const ^Byte PRINT-AS-STRING-MESSAGE-ID 17)
(def ^:const ^Byte LOG-MESSAGE-ID 6)
(def ^:const ^Byte ECHO-MESSAGE-ID 4)
(def ^:const ^Byte ECHO-RESPONSE-MESSAGE-ID 5)

(defn is-valid? [message]
   {:pre  [(seqable? message)
           (every? byte? message)]
    :post [(boolean? %)]}
   true)

(defmulti handle-tcp is-valid?)
(defmulti handle-valid-tcp-message)
(defmulti handle-invalid-tcp-message)

(defmethod handle-tcp true [message]
   (handle-valid-tcp-message message))

(defmethod handle-tcp false [message]
   (handle-invalid-tcp-message message))