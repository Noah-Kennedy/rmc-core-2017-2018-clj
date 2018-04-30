(ns rmc-core-2018-2019.core
  (:gen-class)
  (:import (java.net ServerSocket)))

(def tcp-server (agent (ServerSocket. 2401)))

(defn tcp-handler [])

(defn -main [& args]
  (do
    (send-off tcp-server (fn [server]))
    ()))