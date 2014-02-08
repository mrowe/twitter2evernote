(ns twitter2evernote.evernote-xml
  (:require [clojure.data.xml :as xml])
  (:import java.text.SimpleDateFormat
           java.util.TimeZone
           java.util.Date))

(def date-formatter (SimpleDateFormat. "yyyyMMdd'T'HHmmss'Z'"))
(.setTimeZone date-formatter (TimeZone/getTimeZone "UTC"))

(defn- format-date [date] (.format date-formatter date))

(defn- now [] (format-date (Date.)))

(defn- wrap
  "Wrap string s in xml-ish tag"
  [tag s]
  (str "<" tag ">" s "</" tag ">"))

(defn- content-string
  [body]
  (str 
   "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
   "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">"
   (wrap "en-note"
         (wrap "body" body))))

(defn- entry-element
  [body year]
  (xml/element :note {}
               (xml/element :title {} (str "Twitter Archive " year))
               (xml/element :content {} (xml/cdata (content-string body)))))

(defn evernote-doc
  ""
  [body year]
  (xml/element :en-export {:export-date (now)
                           :application "twitter2evernote"
                           :version     "1.0"}
               (entry-element body year)))
