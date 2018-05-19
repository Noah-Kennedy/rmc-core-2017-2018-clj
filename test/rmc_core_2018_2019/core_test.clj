(ns rmc-core-2018-2019.core-test
  (:require [clojure.test :refer :all]
            [rmc-core-2018-2019.core :refer :all]))

"Mutable states:
 arduino,
 is-alive"

(deftest handle-message-test
  (testing "Is alive"
    (is (do (swap! is-alive (fn [_] true))
            (= (handle-new-message "test 1") "test 1")))
    (is (do (swap! is-alive (fn [_] true))
            (= (handle-new-message "test 1 2") "test 1 2")))
    (is (do (swap! is-alive (fn [_] true))
            (= (handle-new-message "test 1 2 3") "test 1 2 3")))
    (is (do (swap! is-alive (fn [_] true))
            (= (handle-new-message "test") "test"))))
  (testing "Kill"
    (is (do (handle-new-message "dsbl")
            (= @is-alive false))
        "Initial enable")
    (is (do (handle-new-message "enbl")
            (= @is-alive true))
        "Initial disable")
    (is (do (handle-new-message "dsbl")
            (= @is-alive false))
        "Disable again")
    (is (do (handle-new-message "enbl")
            (= @is-alive true))
        "Enable again"))
  (testing "Is not alive"
    (is (do (swap! is-alive (fn [_] false))
            (= (handle-new-message "test 1") :dead)))
    (is (do (swap! is-alive (fn [_] false))
            (= (handle-new-message "test 1 2") :dead)))
    (is (do (swap! is-alive (fn [_] false))
            (= (handle-new-message "test 1 2 3") :dead)))
    (is (do (swap! is-alive (fn [_] false))
            (= (handle-new-message "test") :dead)))))