(ns rmc-core-2018-2019.state
   (:require [rmc-core-2018-2019.arduino :refer :all]))

(def arduinoConnection (create-arduino "RMC Core Arduino Connection"
                                       "ttyACM0"
                                       2000))

