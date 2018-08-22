(ns rmc_core_2018_2019.core
   (:gen-class)
   (:require [rmc-core-2018-2019.tcp :refer :all]
             [rmc-core-2018-2019.comms :refer :all]
             [rmc-core-2018-2019.common :refer :all]
             [rmc-core-2018-2019.handlers :refer :all]
             [clojure.core.async :as async]
             [clojure.tools.logging :as log]))

(defn -main []
   (let [server (create-tcp-server! 2401)]
      (consume-async! server handle-new-tcp-packet!)))