(ns transferwise-clj.core
  (:require [clj-http.client :as client]
            [transferwise-clj.constants :as const]))

;BASIC FUNCTIONS AND PREDICATES
(defn is-greater [l-rate r-rate] (> l-rate r-rate))

(defn diff-between [l-value r-value] (- l-value r-value))

(defn is-best-rate 
  "Check if the rate is best than the last fetched one and the best over all time"
  [rate last-rate best-rate] 
  (and (is-greater rate last-rate) 
       (is-greater rate best-rate)))

;ATOMS
(defn new-rate-map [rate amount] {:rate   rate
                                  :amount amount})

(def best-rate (atom (new-rate-map 0 0)))

(defn new-best-rate 
  [rate amount] 
  (reset! best-rate (new-rate-map rate amount)))

(defn post-quote
   "Retrieve the quote object from Transferwise"
   [quote]
   (client/post const/quotes-url {:form-params quote :content-type :json :as :json}))

(defn parse-quote
   "Parses the quote object retrived from Transferwise"
   [{:keys [body]}]
   (let [{rate    :rate
          options :paymentOptions} body]
     (new-rate-map rate (-> options second :targetAmount))))

(defn check-rate
   "Check if actual quote is greater than last one"
   [parsed-quote]
   (let [{actual-rate :rate
          amount      :amount} parsed-quote
         {last-rate :rate} const/last-transaction
         {best-rate :rate} @best-rate]
     
     (if (is-best-rate actual-rate last-rate best-rate)
       (do (new-best-rate actual-rate amount) parsed-quote)
       (do (print-str "This is not the best quote") nil))))

(defn calc-savings
  "Calc the amount of money you should saving performing the transaction today"
  [parsed-quote]
  (let [{actual-amount :amount
         actual-rate   :rate} parsed-quote
        {last-amount :targetAmount
         last-rate   :rate} const/last-transaction]

    {:savings     (diff-between actual-amount last-amount)
     :actual-rate actual-rate
     :last-rate   last-rate}))

(defn formatted-msg
  "This should return a formatted message for telegram"
  [{:keys [savings actual-rate last-rate]}]
  (let [{spacer :spacer
         header :header
         body   :body} const/message]

    (str spacer
         header
         (format body savings actual-rate last-rate)
         spacer)))

(defn send-msg
  "Send message to telegram chanel"
  [message]
  (client/post const/message-url {:form-params {:chat_id const/chat-id :text message}}))

(defn -main
  [& args]
  (-> const/quote
      post-quote
      parse-quote
      check-rate
      calc-savings
      formatted-msg
      send-msg))