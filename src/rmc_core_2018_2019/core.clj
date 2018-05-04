(ns rmc-core-2018-2019.core
  (:gen-class)
  (:require aleph.tcp
            [clojure.string :as string]
            [manifold.stream :as s]
            [eastwood.lint :as e]
            pyro.printer)
  (:import (java.util.regex Pattern)
           (purejavacomm SerialPort)
           (java.io PrintWriter)))


(pyro.printer/swap-stacktrace-engine!)
(set! *warn-on-reflection* true)

(declare arduino)

;TODO test
(defn send-to-arduino [^String message]
  (-> arduino
      .getOutputStream
      (PrintWriter. true)
      (.println message)))

(defmacro defpass [name function]
  `(defn ~name [^String ~'command]
     (fn [~'body]
       (~function
         (string/replace
           (string/join
             " "
             (flatten
               (list ~'command ~'body)))
           "\n"
           "")))))

(defpass direct-send send-to-arduino)

(defpass direct-return (fn [message] message))

(defn lookup-command-handler [^String command]
  (get {"drv"  (direct-send command)
        "test" (direct-return command)}
       command))

(defn handle-new-message [^String message]
  (let [tokens (string/split message (Pattern/compile " "))
        command (first tokens)
        args (rest tokens)]
    ((lookup-command-handler command) args)))

(defn handle-new-connection [stream _]
  (s/consume (fn [bytes]
               (handle-new-message
                 (byte-streams/convert bytes String)))
             stream))


(defn -main []
  (do
    (def arduino (SerialPort.))
    (aleph.tcp/start-server handle-new-connection {:port 2401})))


(defn run-eastwood
  "Run eastwood tests. This will use our already-written tests to check for bad code."
  []
  (e/eastwood {:source-paths ["src"] :test-paths ["test"]}))

(defn run-lint
  "Run lint tests. This will use our already-written tests to check for bad code."
  []
  (e/lint {:source-paths ["src"] :test-paths ["test"]}))