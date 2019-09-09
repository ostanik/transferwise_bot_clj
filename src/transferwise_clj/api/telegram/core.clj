(ns transferwise-clj.api.telegram.core
  (:require [clj-http.client :as client]))

;; TELEGRAM
(defn send-msg
  "Send message to telegram chanel"
  [url chat-id message]
  (client/post url {:form-params {:chat_id chat-id
                                  :text    message}
                    :socket-timeout 1000
                    :connection-timeout 1000}))

(defn formatted-msg
  "This should return a formatted message for telegram"
  [{:keys [spacer header body]} {:keys [savings actual-rate last-rate]}]
  (str spacer
       header
       (format body savings actual-rate last-rate)
       spacer))