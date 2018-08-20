(ns rmc-core-2018-2019.logging
   (:require [clojure.java.io :as io]))

(defn log! [fileName message]
   {:pre [(string? fileName)]}
   (io! (with-open
           [logger (io/writer fileName :append true)]
           (.write logger
                   (str message "\n")))))