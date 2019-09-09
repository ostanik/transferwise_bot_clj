(ns transferwise-clj.api.transferwise.core
  (:require [clj-http.client :as client]
            [transferwise-clj.data.core])
  (:import [transferwise_clj.data.core Rate]))

(defn post-quote
  "Retrieve the quote object from Transferwise"
  [url quote]
  (client/post url {:form-params  quote
                    :content-type :json
                    :as           :json
                    :socket-timeout 1000
                    :connection-timeout 1000}))

(defn parse-quote
  "Parse transferwise post response into a Rate"
  [{:keys [body]}]
  (let [{rate    :rate
         options :paymentOptions} body]
    (Rate. rate (-> options second :targetAmount))))