(ns net.seabears.rss-finder.core
  (:require [org.httpkit.client :as http])
  (:require [clojure.edn :as edn])
  (:require [clojure.xml :as xml])
  (:require [clojure.java.io :as io])
  (:gen-class))

(defn is-match?
  "Returns whether the RSS item's title matches the specified regex."
  [regex item]
  (->> item
    (filter #(= (:tag %) :title))
    first
    :content
    first
    (re-find (re-pattern (str "(?i)\\b" regex "\\b")))))

(defn find-item
  "Finds an item whose title matches the specified regex."
  [title-regex rss]
  (->> (xml/parse rss)
    :content
    first
    :content
    (filter #(= (:tag %) :item))
    (map #(:content %))
    (filter #(is-match? title-regex %))
    first
    (filter #(= (:tag %) :description))
    first
    :content
    first))

(defn parse-item
  "Returns the captured groups by applying the second regex to the description."
  [desc-regex item]
  (when item
    (drop 1 (re-find (re-pattern (str "(?i)\\b" desc-regex "\\b")) item))))

(defn format-item
  "Returns the format-string formatted with the specified args."
  [fmt args]
  (when args
    (apply (partial format fmt) args)))

(defn to-bytes
  "Converts the specified string to a byte array input stream."
  [s]
  (java.io.ByteArrayInputStream. (.getBytes s)))

(defn read-rss
  "Reads RSS to a string from either the specified file or URL."
  [file url]
  (if file
    (slurp file)
    (:body (deref (http/get url)))))

; configuration
(def env
  (edn/read-string (slurp (str (System/getProperty "user.home") "/.rss-finder/config.edn"))))

(defn -main
  "Reads RSS, picks an item with the specified regex, formats the desription of
   the item, then prints out all the info."
  [& args]
  ; read and parse RSS
  (->>
    (read-rss (:rss-file env) (:rss-url env))
    to-bytes
    (find-item (:title-regex env))
    (parse-item (:description-regex env))
    (format-item (:format-text env))
    println)
  ; print extra info
  (when-let [extra (:extra-output env)]
    (println extra)))
