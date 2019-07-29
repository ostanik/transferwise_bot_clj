(ns transferwise-clj.api.core
  (:require [transferwise-clj.constants :as const]
            [transferwise-clj.data.core :as data]
            [transferwise-clj.api.telegram.core :as telegram]
            [transferwise-clj.api.transferwise.core :as transferwise]))

(defn telegram-send-advise
  "Send the quote advise for the user"
  [savings actual-rate last-rate]
  (telegram/send-msg
   const/message-url
   const/chat-id
   (telegram/formatted-msg
    const/message
    (data/new-telegram-map
     savings 
     actual-rate 
     last-rate))))

(defn transferwise-get-quote
  "Get atual quote from quote model"
  [request-quote-model]
   (transferwise/parse-quote
    (transferwise/post-quote
     const/quotes-url
     request-quote-model)))