(ns rmc-core-2018-2019.comms)

(defprotocol Transmitter
   (transmit! [this message]))

(defprotocol Consumer
   (consume! [this consumerFn])
   (consume-async! [this consumerFb]))

(defn make-message-handler
   "Makes a new message handler function that uses the supplied
   `messageValidator` function to validate the message and then handles the message
   differently based on whether or not it was validated.

   `messageValidator`      := A function that validates the message. This
                              function should always return a boolean.

   `validMessageHandler`   := A function which handles valid messages. This
                              function will be called when a call to the
                              supplied `messageValidator` function returns true.

   `invalidMessageHandler` := A function which handles invalid messages. This
                              function will be called when a call to the
                              supplied messageValidator function returns false."
   [messageValidator
    validMessageHandler
    invalidMessageHandler]
   {:pre  [(fn? validMessageHandler)
           (fn? messageValidator)
           (fn? invalidMessageHandler)]
    :post [(fn? %)]}
   (fn [newMessage]
      (if (messageValidator newMessage)
         (validMessageHandler newMessage)
         (invalidMessageHandler newMessage))))