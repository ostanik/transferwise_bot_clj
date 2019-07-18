(ns transferwise-clj.core
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]
            [environ.core :refer [env]]))

(def chat-id (System/getenv "MASTER-CHAT"))
(def bot-key (System/getenv "BOT-KEY"))
(def message-url (format "https://api.telegram.org/bot%s/sendMessage" bot-key))
(def tw-url "https://transferwise.com/gateway/v2/quotes/")
(def tw-quote {:sourceAmount 5000
               :sourceCurrency "BRL"
               :targetCurrency "EUR"
               :preferredPayIn "BANK-TRANSFER"
               :guaranteedTargetAmount false})

(defn -main
"I don't do a whole lot ... yet."
[& args]
(println "Hello, World!"))

(defn get-quote []
  "Retrieve the quote object from Transferwise"
  (client/post tw-url {:form-params tw-quote :content-type :json :as :json}))

(defn send_msg [chat-id savings rate]
  "Send message to telegram chanel"
  (client/post message-url {:form-params {:chat_id chat-id :text "meu pau de toquinha"}}))