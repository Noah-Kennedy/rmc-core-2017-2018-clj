(ns rmc-core-2018-2019.core
  (:gen-class)
  (:require aleph.tcp))

(defn handle-incoming [message]
  (println message))

(defn -main [& args]
  (aleph.tcp/start-server handle-incoming {:port 2401}))