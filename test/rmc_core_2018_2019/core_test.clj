(ns rmc-core-2018-2019.core-test
   (:require [clojure.test :refer :all]
             [rmc_core_2018_2019.core :refer :all]
             [rmc-core-2018-2019.tcp :refer :all]
             [rmc-core-2018-2019.arduino :refer :all]
             [rmc-core-2018-2019.comms :refer :all]
             [rmc-core-2018-2019.state :refer :all]
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

(deftest test-arduino
   (testing "RX and TX capabilities"
      (transmit! arduinoConnection [1 2 3 4 5])
      (Thread/sleep 1000)
      (is (= [1 2 3 4 5] (vec (receive! arduinoConnection))))
      (is (nil? (receive! arduinoConnection))))
   (println "arduino done"))

(deftest test-common
   (testing "byte?"
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
         (is (not (byte? charNum)))))
   (testing "ref?"
      (let [agentObj (agent nil)
            atomObj  (atom nil)
            refObj   (ref nil)
            obj      nil]
         (is (ref? refObj))
         (is (not (ref? agentObj)))
         (is (not (ref? atomObj)))
         (is (not (ref? obj)))))
   (testing "agent?"
      (let [agentObj (agent nil)
            atomObj  (atom nil)
            refObj   (ref nil)
            obj      nil]
         (is (not (agent? refObj)))
         (is (agent? agentObj))
         (is (not (agent? atomObj)))
         (is (not (agent? obj)))))
   (testing "add-sync!"
      (let [items (ref [])]
         (add-sync! items 5)
         (is (in? (deref items) 5))
         (add-sync! items 7)
         (is (in? (deref items) 7))
         (is (thrown? Exception (dosync (add-sync! items 9))))
         (is (not (in? (deref items) 9))))))