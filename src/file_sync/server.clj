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


(defn handler [directory ch client-info]
  (println "Client connected" client-info)
  (receive-all ch
    #(let [message (read-string %)]
       (println "Serverd" message)
       (handle-message ch message)))
  (watch-files directory (fn [files-changes]
                        (enqueue ch
                                 (pr-str  {:type :files
                                           :files (to-files-map files-changes)})))))

(defn start-server [directory port]
  (tcp/start-tcp-server (partial handler directory) {:port (if (string? port) (Integer/parseInt port) port)
                                                     :frame (string :utf-8 :delimiters ["\r\n"])})
  (println "Server listens on port" port "directory" directory))


;(start-server "src" 12345)

