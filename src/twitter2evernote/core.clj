(ns twitter2evernote.core
  (:require [clojure.data.xml :as xml]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [markdown.core :as md])
  (:import java.text.SimpleDateFormat
           java.util.Date)
  (:gen-class))

(use 'twitter2evernote.evernote-xml)


(defn input-files
  [dir]
  (filter #(.endsWith (.getName %) ".js") (file-seq (clojure.java.io/file dir))))

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
  (let [id (e :id)
        text (e :text)
        timestamp (e :created_at)]
  {:id id
   :title text
   :timestamp (parse-date timestamp)
   :body (body id text timestamp)}))

(defn- read-json
  "Read a twitter js file as json (stripping off the first line)"
  [file]
  (let [js (slurp file)
        json (clojure.string/replace-first js #"^.*\n" "")]
    (json/read-json json)))

(defn- entries
  "A seq of entries read from file"
  [file]
  (println (str "Processing file: " file))
  (let [e (map entry (read-json file))]
    (println (str "    processed " (count e) " entries"))
    e))

(defn twitter-entries
  [dir]
  (let [files (input-files dir)]
    (flatten (map entries files))))

(defn- html-content
  "Parse content as markdown and return html"
  [content]
  (md/md-to-html-string content))

(defn- clean
  "Remove dodgy low-ascii characters"
  [text]
  (clojure.string/replace text #"[\u0000-\u0008\u000b\u000c\u000e-\u001f]" ""))

(defn twitter-entry-to-evernote
  "Return a data map for entry"
  [entry]
  {:title   (clean (entry :title))
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
                           (println "Usage: $0 <input-dir> <output-file>") 
                           (System/exit 1)))
 
  (let [[in out] args]
    (with-open [out-file (java.io.FileWriter. out)]
      (xml/emit (evernote-doc (map twitter-entry-to-evernote (twitter-entries in))) out-file)
      (println "Wrote Evernote export file to " out))))
