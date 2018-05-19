(ns rmc-core-2018-2019.core
  (:gen-class)
  (:require aleph.tcp
            [clojure.string :as string]
            [manifold.stream :as s]
            pyro.printer)
  (:import (java.util.regex Pattern)
           (purejavacomm CommPortIdentifier CommPort)
           (java.util Scanner)
           (java.io PrintWriter)))


(pyro.printer/swap-stacktrace-engine!)
(set! *warn-on-reflection* true)


(def ^String arduino-com-port "ttyACM0")

(declare arduino-printer)
(declare arduino-reader)

(def is-alive (atom false))

(defn send-to-arduino
  "Sends a message over the Arduino serial interface to the Arduino."
  [^String message]
  (when @is-alive
    (send-off arduino-printer
              (fn [^PrintWriter printer]
                (do (.println printer message)
                    printer)))))

(defn print-from-arduino
  "Prints out the most recently sent message from the arduino.
  Used mainly for test code."
  []
  (send-off arduino-reader
            (fn [^Scanner reader]
              (do
                (-> reader
                    .nextLine
                    println)
                reader))))

(defmacro defpass
  "Defines a pass-through function which will take in the
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

(defpass sendoff-to-arduino send-to-arduino)

(defpass direct-return (fn [message]
                         (if @is-alive
                           message
                           :dead)))

(defn lookup-command-handler
  "Looks up the command handler in a map using the command provided.
  Returns back a command which will take in the arguments of the
  command in vector form."
  [^String command]
  (get {"drv"  (sendoff-to-arduino command)
        "test" (direct-return command)
        "intk" (sendoff-to-arduino command)
        "dmp"  (sendoff-to-arduino command)
        "dsbl" (fn [_] (swap! is-alive (fn [_] false)))
        "enbl" (fn [_] (swap! is-alive (fn [_] true)))
        "mt"   (sendoff-to-arduino command)}
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
  (do
    (s/consume
      (fn [bytes]
        (-> bytes
            (byte-streams/convert String)
            handle-new-message))
      stream)))

(defn -main
  "Starts the TCP server and creates Arduino
  reader (Scanner) and printer (PrintWriter) agents."
  []
  (do
    (let [arduino (-> (CommPortIdentifier/getPortIdentifier arduino-com-port)
                      (.open "Arduino Comms" 2000))]
      (swap! is-alive true)
      (def arduino-printer
        (agent
          (-> ^CommPort arduino
              .getOutputStream
              (PrintWriter. true))))
      (def arduino-reader
        (agent
          (-> ^CommPort arduino
              .getInputStream
              Scanner.))))
    (aleph.tcp/start-server handle-new-connection {:port 2401})))