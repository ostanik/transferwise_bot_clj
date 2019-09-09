(ns transferwise-clj.api.core
  (:require [transferwise-clj.constants :refer [message-url quotes-url chat-id message]]
            [transferwise-clj.api.telegram.core :refer [formatted-msg send-msg]]
            [transferwise-clj.api.transferwise.core :as tw])
  (:import [transferwise_clj.data.core Telegram]))

(defn telegram-send-advise
  "Send the quote advise for the user"
  [savings actual-rate last-rate]
  (->> (Telegram. savings actual-rate last-rate)
       (formatted-msg message)
       (send-msg message-url chat-id)))

(defn transferwise-get-quote
  "Get atual quote from quote model"
  [request-quote-model]
  (->> request-quote-model
       (tw/post-quote quotes-url)
       (tw/parse-quote)))