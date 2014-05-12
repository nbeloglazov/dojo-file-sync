(ns file-sync.client
  (:require [aleph.tcp :as tcp]
            [lamina.core :refer :all]
            [gloss.core :refer [string]]))


(defn write-to-file [base filename contents]
  (let [file-out (java.io.File. (str base "/" filename))
        dirs (.getParent file-out)]
    (.mkdirs (java.io.File. dirs))
    (spit file-out contents)))

(defn msg-handler
  [msg directory]
  (let [message (read-string msg)]
    (doseq [[filename contents] (message :files)]
      (println "Updating" filename)
      (write-to-file directory filename contents))))


(defn start-client [directory host port]
  (let [ch (wait-for-result
             (tcp/tcp-client {:host host
                              :port (if (string? port) (Integer/parseInt port) port)
                              :frame (string :utf-8 :delimiters ["\r\n"])}))]
    (receive-all ch #(msg-handler % directory))
    (println "Client connected")
    ch))

;(start-client "tmp2" "localhost" 10000)
;(start-client "backup" "localhost" 12345)
