(ns rmc-core-2018-2019.core
   (:require pyro.printer
             [rmc-core-2018-2019.network.network-core :as net])
   (:gen-class))

(set! *warn-on-reflection* true)
(pyro.printer/swap-stacktrace-engine!)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; main
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn -main []
   (let [tcpClients (net/launch-tcp-server!)]))