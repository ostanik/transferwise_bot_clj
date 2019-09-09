(ns transferwise-clj.data.core)

(defrecord Rate [rate amount])
(defrecord Telegram [savings actual-rate last-rate])