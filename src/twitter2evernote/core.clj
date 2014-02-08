(ns twitter2evernote.core
  (:require [clojure.data.xml :as xml]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [markdown.core :as md])
  (:import java.text.SimpleDateFormat
           java.util.Date)
  (:gen-class))

(use 'twitter2evernote.evernote-xml)

(defn- matches-year?
  [file year]
  (and (.startsWith file year)
       (.endsWith file ".js")))

(defn input-files
  [dir year]
  (filter #(matches-year? (.getName %) year) (file-seq (clojure.java.io/file dir))))

;; lifted from hiccup
(defn- escape
  "Change special characters into XML character entities."
  [s]
  (if (nil? s)
    ""
    (.. ^String s
        (replace "&"  "&amp;")
        (replace "<"  "&lt;")
        (replace ">"  "&gt;")
        (replace "\"" "&quot;"))))

(defn- clean
  "Remove dodgy low-ascii characters"
  [text]
  (clojure.string/replace text #"[\u0000-\u0008\u000b\u000c\u000e-\u001f]" ""))

(defn- body
  [id text timestamp]
  (str
   "<blockquote class=\"twitter-tweet\" lang=\"en\">"
   "<p>" (-> text escape clean) "</p>"
   "&mdash; Michael Rowe (@mrowe) <a href=\"https://twitter.com/mrowe/status/" id "\">" timestamp "</a>"
   "</blockquote>"
   "<br/>"
   "via Twitter https://twitter.com/mrowe"
   "<br/><br/>"
   timestamp
   "<br/><br/>"
   "-----<br/><br/>"))

(defn entry
  [ { :keys [id text created_at] } ]
    (body id text created_at))

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
  (let [e (reverse (map entry (read-json file)))]
    (println (str "    processed " (count e) " entries"))
    e))

(defn twitter-entries
  [dir year]
  (let [files (input-files dir year)]
    (flatten (map entries files))))

(defn -main
  ""
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (if (< (count args) 2) (binding [*out* *err*]
                           (println "Usage: $0 <input-glob> <year> <output-file>")
                           (System/exit 1)))
 
  (let [[in year out] args
        body (apply str (twitter-entries in year))]
    (with-open [out-file (java.io.FileWriter. out)]
      (xml/emit (evernote-doc body year) out-file)
      (println "Wrote Evernote export file to" out))))
