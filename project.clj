(defproject twitter2evernote "0.2.1"
  :description "Read Day One entry files and write an Evernote export file."
  :url "https://github.com/mrowe/twitter2evernote"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.xml "0.0.7"]
                 [org.clojure/data.json "0.2.4"]
                 [joda-time "1.6"]
                 [commons-codec/commons-codec "1.4"]
                 [markdown-clj "0.9.21"]]
  :main twitter2evernote.core)
