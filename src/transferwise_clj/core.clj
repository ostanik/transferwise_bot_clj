(ns transferwise-clj.core
  (:require [transferwise-clj.data.atoms :refer [best-rate new-best-rate]]
            [transferwise-clj.constants  :refer [last-transaction quote]]
            [transferwise-clj.api.core   :refer [transferwise-get-quote telegram-send-advise]])
  (:import [transferwise_clj.data.core Telegram]))

;PREDICATES
(defn is-greater? 
  [l-rate r-rate] 
  (> l-rate r-rate))

(defn is-best-rate?
  [rate last-rate best-rate]
  (and (is-greater? rate last-rate)
       (is-greater? rate best-rate)))

(defn diff-between 
  [l-value r-value] 
  (- l-value r-value))

(defn check-best-rate
   "Check if actual quote is greater than last one"
   [{:keys [amount rate]}]
   (let [{last-rate :rate} last-transaction
         {best-rate :rate} @best-rate]
     (if (is-best-rate? rate last-rate best-rate)
       (do (new-best-rate rate amount))
       (do (println "This is not the best quote") nil))))

(defn calc-savings
  "Calc the amount of money you should saving performing the transaction today"
  [{:keys [amount rate]}]
  (let [{last-amount :targetAmount
         last-rate   :rate} last-transaction]
    (Telegram. (diff-between amount last-amount) rate last-rate)))

(defn -main
  [& args]
  (let [best-rate (-> quote
                      transferwise-get-quote
                      check-best-rate)]
    (if best-rate
      (do (let [{:keys [savings
                        actual-rate
                        last-rate]} (-> best-rate calc-savings)]
            (telegram-send-advise savings actual-rate last-rate)))
      (do (println "Isn't the best rate ever")))))