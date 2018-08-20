(ns rmc-core-2018-2019.common
   (:require [clojure.tools.logging :as log])
   (:import (clojure.lang Ref Agent)))

(defn in?
   "true if coll contains elm"
   [coll elm]
   (some #(= elm %) coll))

(defn byte? [x]
   {:post [(boolean? %)]}
   (instance? Byte x))

(defn ref? [x]
   {:post [(boolean? %)]}
   (instance? Ref x))

(defn agent? [x]
   {:post [(boolean? %)]}
   (instance? Agent x))

(defn add-sync! [collection element]
   {:pre  [(ref? collection)
           (sequential? (deref collection))]
    :post [(in? (deref collection) element)]}
   (io! (dosync (alter collection conj element))))