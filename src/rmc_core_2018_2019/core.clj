(ns rmc-core-2018-2019.core
  (:gen-class)
  (:require aleph.tcp
            [clojure.string :as string]
            [manifold.stream :as s]
            [eastwood.lint :as e]
            pyro.printer)
  (:import (java.util.regex Pattern)))

(pyro.printer/swap-stacktrace-engine!)
(set! *warn-on-reflection* true)

(declare arduino-serial-agent)

(def mode (atom :manual))

;TODO implement
(defn send-to-arduino [^String message]
  (println (str "Sending '" message "' to Arduino.")))

(defmacro direct-send [^String command]
  `(fn [~'body]
     (send-to-arduino
       (string/replace
         (string/join
           " "
           (flatten
             (list ~'command ~'body)))
         "\n"
         ""))))

(defn lookup-command-handler [^String command]
  (get {"drv"  (direct-send command)
        "intk" (direct-send command)
        "auto" (fn [] (swap! mode :auto))
        "man"  (fn [] (swap! mode :manual))}
       command))

(defn accept-message [^String message]
  (let [tokens (string/split message (Pattern/compile " "))
        ^String command (first tokens)
        args (rest tokens)]
    ((lookup-command-handler command) args)))

(defn handle-incoming [stream _]
  (s/consume (fn [bytes]
               (time
                 (accept-message
                   (byte-streams/convert bytes String))))
             stream))

(defn -main []
  (aleph.tcp/start-server handle-incoming {:port 2401}))

(defn run-eastwood
  "Run eastwood tests. This will use our already-written tests to check for bad code."
  []
  (do
    (e/eastwood {:source-paths ["src"] :test-paths ["test"]})
    (e/lint {:source-paths ["src"] :test-paths ["test"]})))