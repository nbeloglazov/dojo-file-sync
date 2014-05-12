(ns file-sync.server
  (:require [aleph.tcp :as tcp]
            [lamina.core :refer :all]
            [gloss.core :refer [string]]))

(def files (atom {"one.txt" "hello"
                  "two.txt" "world"}))


(defmulti handle-message (fn [channel message] (:type message)))

(defmethod handle-message :get-all-files
  [channel message]
  (enqueue channel (pr-str {:type :files
                            :files @files})))

(defn handler [ch client-info]
  (receive-all ch
    #(do (println "Server" %)
         (enqueue ch (str "You said " %)))))

(tcp/start-tcp-server #'handler {:port 10000 :frame (string :utf-8 :delimiters ["\r\n"])})

