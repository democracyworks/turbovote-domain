(ns turbovote-domain.election-authority.util
  (:require [clojure.spec :as s]
            [turbovote-domain.election-authority :as authority]))

(defn authority->name
  "Given an authority, returns the best possible name to
  communicate to users."
  [authority]
  (let [conformed (s/conform ::authority/nameable authority)]
    (if (= ::s/invalid conformed)
      "Election Office"
      (condp = (first conformed)
        ::authority/official-title (::authority/official-title authority)
        ::authority/office-name (::authority/office-name authority)))))

(defn authority->preferred-address
  "Given an authority, returns the best possible address to
  give to users."
  [authority]
  (let [conformed (s/conform ::authority/addressable authority)]
    (when-not (= ::s/invalid conformed)
      (condp = (first conformed)
        ::authority/mailing (::authority/mailing-address authority)
        ::authority/physical (::authority/physical-address authority)))))

(defn registration-authority?
  [authority]
  (if (s/valid? ::authority/typable authority)
    (contains? (::authority/types authority) ::authority/registration)))

(defn election-authority?
  [authority]
  (if (s/valid? ::authority/typable authority)
    (contains? (::authority/types authority) ::authority/election)))
