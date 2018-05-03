(ns rmc-core-2018-2019.core-test
  (:require [clojure.test :refer :all]
            [rmc-core-2018-2019.core :refer :all]))

(deftest accept-message-test
  (testing "Direct return"
    (is (= (accept-message "test 1") "test 1"))
    (is (= (accept-message "test 2") "test 2"))
    (is (= (accept-message "test 3") "test 3"))
    (is (= (accept-message "test") "test"))))