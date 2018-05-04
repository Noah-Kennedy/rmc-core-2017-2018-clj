(ns rmc-core-2018-2019.core-test
  (:require [clojure.test :refer :all]
            [rmc-core-2018-2019.core :refer :all]))

(deftest handle-message-test
  (testing "Direct return"
    (is (= (handle-new-message "test 1") "test 1"))
    (is (= (handle-new-message "test 1 2") "test 1 2"))
    (is (= (handle-new-message "test 1 2 3") "test 1 2 3"))
    (is (= (handle-new-message "test") "test"))))