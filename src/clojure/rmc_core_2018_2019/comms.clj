(ns rmc-core-2018-2019.comms)

(defprotocol Transmitter
   (transmit! [this message]))

(defprotocol Receiver
   (has-new? [this])
   (receive! [this]))

(defprotocol Consumer
   (consume! [this consumerFn])
   (consume-async! [this consumerFb]))