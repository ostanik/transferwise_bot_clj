(ns transferwise-clj.core
  (:require [clj-http.client :as client]
            [environ.core :refer [env]]))
;ENV
(def chat-id (env :master-chat))
(def bot-key (env :bot-key))

; This should be retrieved from DB on the future
(def last-transaction{:rate 0.23345
                      :targetAmount 1138.07
                      :sourceAmount 5000
                      :sourceCurrency "BRL"
                      :targetCurrency "EUR"})

; API Constants
(def message-url (format "https://api.telegram.org/bot%s/sendMessage" bot-key))
(def quotes-url "https://transferwise.com/gateway/v2/quotes/")

(def quote {:sourceAmount (last-transaction :sourceAmount)
            :sourceCurrency (last-transaction :sourceCurrency)
            :targetCurrency (last-transaction :targetCurrency)
            :preferredPayIn "BANK-TRANSFER"
            :guaranteedTargetAmount false})

(def message {:spacer "\n-------\n\n"
              :header (format "The %s => %s rate is really good\n\n"
                              (last-transaction :sourceCurrency)
                              (last-transaction :targetCurrency))
              :body (str "You can earn more %.2f "
                         (last-transaction :targetCurrency)
                         " since your last transfer\n\n Actual rate: %.5f\n Last rate: %.5f")})

(defn post-quote
  "Retrieve the quote object from Transferwise"
  [quote]
  (client/post quotes-url {:form-params quote :content-type :json :as :json}))

(defn parse-quote
  "Parses the quote object retrived from Transferwise"
  [{:keys [body]}]
  (let [{rate :rate options :paymentOptions} body]
    {:actual-rate rate :amount (-> options second :targetAmount)}))

(defn is-greater
  "Check if the first rate is greater than the other"
  [l-rate r-rate]
  (> l-rate r-rate))

(defn check-rate
  "Check if actual quote is greater than last one"
  [parsed-quote]
  (let [{actual-rate :actual-rate} parsed-quote
        {last-rate :rate} last-transaction]
    (if (is-greater actual-rate last-rate) parsed-quote)))

(defn calc-savings
  "Calc the amount of money you should saving performing the transaction today"
  [parsed-quote]
  (let [{actual-amount :amount
         actual-rate :actual-rate} parsed-quote
        {last-amount :targetAmount
         last-rate :rate} last-transaction]
    {:savings (- actual-amount last-amount)
     :actual-rate actual-rate
     :last-rate last-rate}))

(defn formatted-msg
  "This should return a formatted message for telegram"
  [bodyParams]
  (let [{savings :savings
         actual-rate :actual-rate
         last-rate :last-rate} bodyParams]
    (str (message :spacer)
         (message :header)
         (format (message :body) savings actual-rate last-rate)
         (message :spacer))))

(defn send-msg
  "Send message to telegram chanel"
  [message]
  (client/post message-url {:form-params {:chat_id chat-id :text message}}))

(defn -main
  [& args]
  (-> quote
      post-quote
      parse-quote
      check-rate
      calc-savings
      formatted-msg
      send-msg))