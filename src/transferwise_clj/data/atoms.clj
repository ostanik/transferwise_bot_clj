(ns transferwise-clj.data.atoms 
  (:require [transferwise-clj.data.core :as core]))

;;ATOMS
(def best-rate (atom (core/new-rate-map 0 0)))

(defn new-best-rate
  [rate amount]
  (reset! best-rate (core/new-rate-map rate amount)))
