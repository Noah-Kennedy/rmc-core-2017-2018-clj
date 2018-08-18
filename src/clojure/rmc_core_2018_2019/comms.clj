(ns rmc-core-2018-2019.comms)

(defprotocol Transmitter
   (transmit! [this message]))

(defprotocol Consumer
   (consume! [this consumerFn])
   (consume-async! [this consumerFb]))