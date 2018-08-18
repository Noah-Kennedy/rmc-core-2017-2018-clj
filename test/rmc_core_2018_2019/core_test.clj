(ns rmc-core-2018-2019.core-test
   (:require [clojure.test :refer :all]
             [rmc_core_2018_2019.core :refer :all]
             [rmc-core-2018-2019.tcp :refer :all]))

(deftest test-tcp
   (testing "TCP Server Creation"
      (let [realServer   (create-tcp-server! 1337)
            nilServer    nil
            zeroServer   0
            stringServer "Test"]
         (is (tcp-server? realServer))
         (is (not (tcp-server? nilServer)))
         (is (not (tcp-server? zeroServer)))
         (is (not (tcp-server? stringServer))))))