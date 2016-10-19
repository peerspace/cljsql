(ns cljsql.core
  (:require [hugsql.core :as hugsql]
            [opennlp.nlp :as nlp]
            [opennlp.treebank :as treebank]
            [opennlp.tools.filters :as filters]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(def pg-db {:dbtype "postgresql"
            :dbname "mosql"
            :host "localhost"
            :user "peerspace"
            :password "secret"})

(hugsql/def-db-fns "sql/pull.sql")

(def inq (inquiries pg-db))

(def get-sentences (nlp/make-sentence-detector "models/en-sent.bin"))
(def tokenize (nlp/make-tokenizer "models/en-token.bin"))
(def pos-tag (nlp/make-pos-tagger "models/en-pos-maxent.bin"))
(def name-find (nlp/make-name-finder "models/en-ner-person.bin" "models/en-ner-organization.bin"))

(defn process-text [text]
  (let [text (clojure.string/replace text "\n" " ")
        sentences (get-sentences text)]
    (for [sentence sentences]
      (let [tokens (tokenize sentence)
            names (name-find tokens)
            ptag (pos-tag tokens)]
        {:sentence sentence
         :names names
         :verbs (filters/verbs ptag)
         :nouns (filters/nouns ptag)
         :verb-phrases (filters/verb-phrases ptag)
         :proper-nouns (filters/proper-nouns ptag)
         }))))
