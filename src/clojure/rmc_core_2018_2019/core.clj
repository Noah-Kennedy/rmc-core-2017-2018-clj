(ns rmc_core_2018_2019.core
   (:gen-class)
   (:require pyro.printer
             [rmc-core-2018-2019.tcp :as net]
             [rmc-core-2018-2019.common :refer :all]
             [clojure.core.async :as async]))

(defn init []
   (pyro.printer/swap-stacktrace-engine!))

(defn launch-framework! [])

(defn -main []
   (init)
   (let [TCP (net/launch-server!)]
      (launch-framework!)))