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

(defn write-to-file [filename contents]
  (let [file-out (java.io.File. (str "client/" filename))
        dirs (.getParent file-out)]
    (.mkdirs (java.io.File. dirs))
    (spit file-out contents)))

(write-to-file "./test/bla.txt" "cool contents")
(defn msg-handler
  [msg]
  (let [message (read-string msg)]
    (println "Client" message)
    (doseq [[filename contents] (message :files)]
      (write-to-file filename contents))
    (reset! files message)))



(receive-all ch #'msg-handler)

(enqueue ch "{:type :get-all-files}")

(close ch)
@files
