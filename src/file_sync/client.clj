(ns file-sync.client
  (:require [aleph.tcp :as tcp]
            [lamina.core :refer :all]
            [gloss.core :refer [string]]
            [clojure.pprint :refer [pprint]]))

(def ch
  (wait-for-result
   (tcp/tcp-client {:host "localhost"
                    :port 10000
                    :frame (string :utf-8 :delimiters ["\r\n"])})))


(def files (atom nil))


(defn msg-handler
  [msg]
  (let [message (read-string msg)]
    (println "Client" message)
    (reset! files message)))



(receive-all ch #'msg-handler)

(enqueue ch "{:type :get-all-files}")

@files
