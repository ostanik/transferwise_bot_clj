(ns transferwise-clj.constants
  (:require [environ.core :refer [env]]))

;ENV
(def chat-id (env :master-chat))
(def bot-key (env :bot-key))

;; URLS
(def message-url (format "https://api.telegram.org/bot%s/sendMessage" bot-key))
(def quotes-url "https://transferwise.com/gateway/v2/quotes/")

(def last-transaction{:rate           0.23345
                      :targetAmount   1138.07
                      :sourceAmount   5000
                      :sourceCurrency "BRL"
                      :targetCurrency "EUR"})

(def quote {:sourceAmount           (last-transaction :sourceAmount)
            :sourceCurrency         (last-transaction :sourceCurrency)
            :targetCurrency         (last-transaction :targetCurrency)
            :preferredPayIn         "BANK-TRANSFER"
            :guaranteedTargetAmount false})

(def message {:spacer "\n-------\n\n"
              :header (format "The %s => %s rate is really good\n\n"
                              (last-transaction :sourceCurrency)
                              (last-transaction :targetCurrency))
              :body   (str "You can earn more %.2f "
                           (last-transaction :targetCurrency)
                           " since your last transfer\n\n Actual rate: %.5f\n Last rate: %.5f")})