(ns turbovote-domain.election-authority
  (:require [clojure.spec :as s]
            [clojure.string :as str]))

(s/def ::office-name string?)
(s/def ::official-title string?)

(s/def ::nameable (s/or ::official-title (s/keys :req [::official-title])
                        ::office-name (s/keys :req [::office-name])))

(s/def ::street (s/and string? #(not (str/blank? %))))
(s/def ::address (s/keys :req [::street]))
(s/def ::mailing-address ::address)
(s/def ::physical-address ::address)

(s/def ::addressable (s/or ::mailing (s/keys :req [::mailing-address])
                           ::physical (s/keys :req [::physical-address])))

(s/def ::registration (s/and keyword? #(= :registration)))
(s/def ::election (s/and keyword? #(= :election)))

(s/def ::authority-types #{::registration ::election})
(s/def ::types (s/coll-of ::authority-types :kind set?))
(s/def ::typable (s/keys :req [::types]))
