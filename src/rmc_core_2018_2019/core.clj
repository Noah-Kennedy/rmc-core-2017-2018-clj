(ns rmc-core-2018-2019.core
  (:gen-class)
  (:require aleph.tcp
            [clojure.string :as string])
  (:import (java.util.regex Pattern)))

(declare arduino-serial-agent)

;TODO implement
(defn send-to-arduino [message]
  (println (str "Sending '" message "' to Arduino.")))

(def mode (atom :manual))

(defmacro direct-send [command]
  `(fn [~'body]
     (send-to-arduino
       (string/join
         " "
         (flatten
           (list ~'command ~'body))))))

(defn lookup-command-handler [command]
  (get {"drv"  (direct-send command)
        "intk" (direct-send command)
        "auto" (fn [] (swap! mode :auto))
        "man"  (fn [] (swap! mode :manual))}
       command))

(defn handle-incoming [message]
  (let [tokens (string/split message (Pattern/compile " "))
        command (first tokens)
        args (rest tokens)]
    (trampoline (lookup-command-handler command) args)))

(defn print-incoming [message]
  (println message))

(defn -main []
  (aleph.tcp/start-server handle-incoming {:port 2401}))

