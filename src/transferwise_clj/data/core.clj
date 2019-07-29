(ns transferwise-clj.data.core)

(defn new-rate-map
  [rate amount]
  {:rate   rate
   :amount amount})

(defn new-telegram-map
  [savings actual-rate last-rate]
  {:savings    savings
   :actual-rate actual-rate
   :last-rate  last-rate})