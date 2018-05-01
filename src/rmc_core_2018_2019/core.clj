(ns rmc-core-2018-2019.core
  (:gen-class)
  (:require aleph.tcp))

(declare arduino-serial-agent)

(defn send-to-arduino [message])

(def mode (atom :manual))

(defmacro direct-send [command]
  `(fn [~'message]
     (send-to-arduino (str ~command ~'message))))

(defn lookup-command-handler [command]
  (get {"drv"  (direct-send command)
        "intk" (direct-send command)
        "auto" (fn [] (swap! mode :auto))
        "man"  (fn [] (swap! mode :manual))}
       command))

(defn handle-incoming [message]
  ())

(defn print-incoming [message]
  (println message))

(defn -main [& args]
  (aleph.tcp/start-server handle-incoming {:port 2401}))

