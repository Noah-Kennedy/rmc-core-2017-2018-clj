(ns rmc-core-2018-2019.core-test
   (:require [clojure.test :refer :all]
             [rmc_core_2018_2019.core :refer :all]
             [rmc-core-2018-2019.tcp :refer :all]
             [rmc-core-2018-2019.common :refer :all]))

(deftest test-tcp
   (testing "Create TCP Server"
      (let [realServer   (create-tcp-server! 1337)
            nilServer    nil
            zeroServer   0
            stringServer "Test"]
         (is (tcp-server? realServer))
         (is (not (tcp-server? nilServer)))
         (is (not (tcp-server? zeroServer)))
         (is (not (tcp-server? stringServer))))))

(deftest test-common
   (testing "Byte?"
      (let [intNum    (int 4)
            byteNum   (byte 4)
            longNum   (long 4)
            shortNum  (short 4)
            stringNum "4"
            charNum   \4]
         (is (byte? byteNum))
         (is (not (byte? intNum)))
         (is (not (byte? longNum)))
         (is (not (byte? shortNum)))
         (is (not (byte? stringNum)))
         (is (not (byte? charNum))))))