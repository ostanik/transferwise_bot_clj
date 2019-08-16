(ns transferwise-clj.data.atoms
  (:require [transferwise-clj.data.core])
  (:import [transferwise_clj.data.core Rate]))

;;ATOMS
(def best-rate (atom (Rate. 0 0)))

(defn new-best-rate
  [rate amount]
  (reset! best-rate (Rate. rate amount)))
