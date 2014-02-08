(ns twitter2evernote.evernote-xml-test
  (:require [clojure.data.xml :as xml])
  (:use clojure.test
        twitter2evernote.evernote-xml)
  (:import java.util.Date))

(deftest evernote-doc-test

  (let [now (Date.)
        entry "<p>Some content.</p>\n<p>More content.</p>"
        enex (xml/emit-str (evernote-doc [entry] "2014"))]

    ;; this is all pretty embarassing really
    (testing "Creates xml document"
      (is (re-find #"^<\?xml" enex)))

    (testing "Contains en-export tag"
      (is (re-find #"<en-export " enex)))

    (testing "Contains note tag"
      (is (re-find #"<note>" enex)))

    (testing "Contains the title"
      (is (re-find #"<title>Twitter Archive 2014</title>" enex)))

    (testing "Contains the content as CDATA"
      (is (re-find #"<content><!\[CDATA\[" enex)))

    (testing "Contains HTML tags in the content"
      (is (re-find #"<p>Some content.</p>" enex)))))
