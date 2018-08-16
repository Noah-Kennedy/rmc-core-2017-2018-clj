(ns rmc-core-2018-2019.common
   (:require [clojure.java.io :as io])
   (:import (clojure.lang Ref)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Message IDs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def ^:const ^Byte PRINT-AS-BYTES-MESSAGE-ID 16)
(def ^:const ^Byte PRINT-AS-STRING-MESSAGE-ID 17)
(def ^:const ^Byte LOG-MESSAGE-ID 6)
(def ^:const ^Byte ECHO-MESSAGE-ID 4)
(def ^:const ^Byte ECHO-RESPONSE-MESSAGE-ID 5)

(defn log! [fileName message]
   {:pre [(string? fileName)]}
   (io! (with-open
           [logger (io/writer fileName :append true)]
           (.write logger
                   (str message "\n")))))

(defn byte? [x]
   {:post [(boolean? %)]}
   (instance? Byte x))

(defn ref? [x]
   {:post [(boolean? %)]}
   (instance? Ref x))

(defn add-sync! [collection element]
   {:pre [(ref? collection)
          (sequential? (deref collection))]
    ;:post [(contains? (deref collection) element)]
    }
   (io! (dosync (alter collection conj element))))