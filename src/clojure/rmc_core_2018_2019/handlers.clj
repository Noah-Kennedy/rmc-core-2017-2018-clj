(ns rmc-core-2018-2019.handlers
   (:require [rmc-core-2018-2019.common :refer :all]
             [clojure.tools.logging :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Message IDs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ^:const ^Byte LOG-MESSAGE-ID 6)
(def ^:const ^Byte ECHO-MESSAGE-ID 4)
(def ^:const ^Byte ECHO-RESPONSE-MESSAGE-ID 5)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Message validation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn is-valid? [message]
   {:pre  [(seqable? message)
           (every? byte? message)]
    :post [(boolean? %)]}
   true)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Multi definitions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmulti handle-tcp-message! is-valid?)
(defmulti handle-valid-tcp-message! first)
(defmulti handle-invalid-tcp-message! first)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Message validation sorting
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmethod handle-tcp-message! true [message]
   {:pre [(seqable? message)
          (every? byte? message)]}
   (handle-valid-tcp-message! message))

(defmethod handle-tcp-message! false [message]
   {:pre [(seqable? message)
          (every? byte? message)]}
   (handle-invalid-tcp-message! message))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Valid message handling
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmethod handle-valid-tcp-message! LOG-MESSAGE-ID [message]
   {:pre [(seqable? message)
          (every? byte? message)]}
   (log/info (str "Received new log message: [" message "]")))

(defmethod handle-valid-tcp-message! :default [message]
   {:pre [(seqable? message)
          (every? byte? message)]}
   (log/warn (str "Received unhandled message: [" message "]")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Invalid message handling
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmethod handle-invalid-tcp-message! :default [message]
   {:pre [(seqable? message)
          (every? byte? message)]}
   (log/error (str "Received invalid message: [" message "]")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Message deconstruction
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn handle-new-tcp-packet! [message]
   {:pre  [(bytes? message)]}
   (-> message
       (vec)
       (handle-tcp-message!)))