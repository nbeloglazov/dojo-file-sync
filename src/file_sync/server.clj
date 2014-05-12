(ns file-sync.server
  (:require [aleph.tcp :as tcp]
            [lamina.core :refer :all]
            [gloss.core :refer [string]]
            [watchtower.core :refer :all]))

(def files (atom {"one.txt" "hello"
                  "two.txt" "world"}))


(defmulti handle-message (fn [channel message] (:type message)))

(defmethod handle-message :get-all-files
  [channel message]
  (enqueue channel (pr-str {:type :files
                            :files @files})))

(defn to-files-map [file-changes]
  (into {}
        (for [file file-changes]
          [(.getPath file) (slurp file)])))

(defn watch-files [directory on-change-fn]
  (watcher [directory]
    (rate 50) ;; poll every 50ms
    (on-change on-change-fn)))


(defn handler [ch client-info]
  (receive-all ch
    #(let [message (read-string %)]
       (println "Serverdfdfd" message)
       (handle-message ch message)))
  (watch-files "test" (fn [files-changes]
                        (enqueue ch
                                 (pr-str  {:type :files
                                           :files (to-files-map files-changes)})))))

(watch-files "test" #(println "Files changed" (to-files-map %)))

(tcp/start-tcp-server #'handler {:port 10000 :frame (string :utf-8 :delimiters ["\r\n"])})

