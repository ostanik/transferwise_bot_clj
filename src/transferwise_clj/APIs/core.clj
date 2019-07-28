(ns transferwise-clj.api.core
  (:require [clj-http.client :as client]
            [transferwise-clj.data.constants :as const]
            [transferwise-clj.data.core :as data]))

;; TRANSFERWISE
(defn post-quote
  "Retrieve the quote object from Transferwise"
  [quote]
  (client/post const/quotes-url {:form-params  quote
                                 :content-type :json
                                 :as           :json}))

(defn parse-quote
  "Parses the quote object retrived from Transferwise"
  [{:keys [body]}]
  (let [{rate    :rate
         options :paymentOptions} body]
    (data/new-rate-map rate (-> options second :targetAmount))))

;; TELEGRAM
(defn send-msg
  "Send message to telegram chanel"
  [message]
  (client/post const/message-url {:form-params {:chat_id const/chat-id
                                                :text    message}}))

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