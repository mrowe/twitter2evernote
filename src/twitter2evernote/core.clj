(ns twitter2evernote.core
  (:require [clojure.data.xml :as xml]
            [clojure.data.csv :as csv]
            [clojure.string :as string]
            [markdown.core :as md])
  (:import java.text.SimpleDateFormat
           java.util.Date)
  (:gen-class))

(use 'twitter2evernote.evernote-xml)

(defn- body
  [id text timestamp]
  (str
   "via Twitter https://twitter.com/mrowe/status/" id
   "<br/>"
   "at " timestamp
   "<br/>"))

(def date-format (SimpleDateFormat. "yyyy-MM-dd HH:mm:ss Z"))
(defn parse-date
  [date]
  (.parse date-format date))
  
(defn entry
  [e]
  (let [id (e 0)
        text (e 5)
        timestamp (e 3)]
  {:id id
   :title text
   :timestamp (parse-date timestamp)
   :body (body id text timestamp)}))

(defn take-csv
  "Takes file name and reads data."
  [fname]
)

(defn twitter-entries
  [file]
  (with-open [file (clojure.java.io/reader file)]
    (map entry (doall (map (comp first csv/read-csv) (line-seq file))))))

;;(twitter-entries "tweets.csv")

(defn- title
  "Derives a title from the content text."
  [text]
  text)

(defn- html-content
  "Parse content as markdown and return html"
  [content]
  (md/md-to-html-string content))

(defn twitter-entry-to-evernote
  "Return a data map for entry"
  [entry]
  (println (str "Processing entry: " (entry :title)))
  {:title   (entry :title)
   :content (html-content (entry :body))
   :content-raw (entry :body)
   :created (entry :timestamp)
   :updated (entry :timestamp)})

(defn -main
  ""
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (if (< (count args) 2) (binding [*out* *err*]
                           (println "Usage: $0 <input-file> <output-file>") 
                           (System/exit 1)))
 
  (let [[in out] args]
    (with-open [out-file (java.io.FileWriter. out)]
      (xml/emit (evernote-doc (map twitter-entry-to-evernote (twitter-entries in))) out-file))
    (str "Wrote Evernote export file to " out)))
