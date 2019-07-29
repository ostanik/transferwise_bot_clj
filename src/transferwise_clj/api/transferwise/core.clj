(ns transferwise-clj.api.transferwise.core
  (:require [clj-http.client :as client]
            [transferwise-clj.data.core :as data]))

(defn post-quote
  "Retrieve the quote object from Transferwise"
  [url quote]
  (client/post url {:form-params  quote
                    :content-type :json
                    :as           :json}))

(defn parse-quote
  "Parse transferwise post response into new-rate-map"
  [{:keys [body]}]
  (let [{rate    :rate
         options :paymentOptions} body]
    (data/new-rate-map rate (-> options second :targetAmount))))