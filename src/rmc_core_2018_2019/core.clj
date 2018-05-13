(ns rmc-core-2018-2019.core
  (:gen-class)
  (:require aleph.tcp
            [clojure.string :as string]
            [manifold.stream :as s]
            [eastwood.lint :as e]
            pyro.printer)
  (:import (java.util.regex Pattern)
           (purejavacomm CommPortIdentifier CommPort)
           (java.io PrintWriter OutputStream)))


(pyro.printer/swap-stacktrace-engine!)
(set! *warn-on-reflection* true)


(def ^String arduino-com-port "COM5")
(declare arduino)


(defn send-to-arduino
  "Sends a message over the arduinoserial interface to the Arduino."
  [^String message]
  (-> ^CommPort arduino
      ^OutputStream .getOutputStream
      (PrintWriter. true)
      (.println message)))


(defmacro defpass
  "Defines a passthrough function which will take in the
  entire command as an argument. This is good for
  situations in which you will be relaying the
  entire command forwards to another device."
  [name function]
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

(defn lookup-command-handler
  "Looks up the command handler in a map using the command provided.
  Returns back a command which will take in the arguments of the
  command in vector form."
  [^String command]
  (get {"drv"  (direct-send command)
        "test" (direct-return command)
        "intk" (direct-send command)
        "dmp"  (direct-send command)
        "kill" (direct-send command)
        "mt"   (direct-send command)}
       command))

(defn handle-new-message
  "Handles a new incoming message that was sent by the GUI."
  [^String message]
  (let [tokens (string/split message (Pattern/compile " "))
        command (first tokens)
        args (rest tokens)]
    ((lookup-command-handler command) args)))

(defn handle-new-connection
  "Handles a new connection request sent by a TCP client."
  [stream _]
  (do (letfn [(handle-arduino-message [message]
                (s/put! stream message))]
        (s/consume
          (fn [bytes]
            (-> bytes
                (byte-streams/convert String)
                handle-arduino-message))
          (-> ^CommPort arduino
              .getInputStream)))
      (s/consume
        (fn [bytes]
          (-> bytes
              (byte-streams/convert String)
              handle-new-message))
        stream)))



(defn -main []
  (do
    (def arduino (-> (CommPortIdentifier/getPortIdentifier arduino-com-port)
                     (.open "Arduino Comms" 2000)))
    (aleph.tcp/start-server handle-new-connection {:port 2401})))


(defn run-eastwood
  "Run eastwood tests. This will use our already-written tests to check for bad code."
  []
  (e/eastwood {:source-paths ["src"] :test-paths ["test"]}))

(defn run-lint
  "Run lint tests. This will use our already-written tests to check for bad code."
  []
  (e/lint {:source-paths ["src"] :test-paths ["test"]}))