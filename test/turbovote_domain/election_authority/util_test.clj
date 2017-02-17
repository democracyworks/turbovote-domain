(ns turbovote-domain.election-authority.util-test
  (:require [turbovote-domain.election-authority.util :as util]
            [turbovote-domain.election-authority :as authority]
            [clojure.test :refer :all]))

(deftest authority->name-test
  (let [test-authority {::authority/official-title "Title"
                        ::authority/office-name "Name"}]
    (testing "returns official-title over all others"
      (is (= "Title" (util/authority->name test-authority))))
    (testing "returns office-name if no title available"
      (is (= "Name" (util/authority->name
                     (dissoc test-authority ::authority/official-title)))))
    (testing "Falls back to Election Office"
      (is (= "Election Office" (util/authority->name {}))))))

(deftest authority->preferred-address-test
  (let [test-authority {::authority/mailing-address
                        {::authority/street "POBox"}
                        ::authority/physical-address
                        {::authority/street "123 Main St"}}]
    (testing "returns mailing-address when available"
      (is (= {::authority/street "POBox"} (util/authority->preferred-address
                                           test-authority))))
    (testing "returns physical when no mailing address available"
      (is (= {::authority/street "123 Main St"}
             (util/authority->preferred-address
              (dissoc test-authority ::authority/mailing-address)))))
    (testing "returns nil when neither available"
      (is (nil? (util/authority->preferred-address {}))))))

(deftest registration-authority?-test
  (let [test-authority {::authority/types #{::authority/registration
                                            ::authority/election}}]
    (testing "true with both types"
      (is (util/registration-authority? test-authority)))
    (testing "true with just registration type"
      (is (util/registration-authority?
           (update test-authority ::authority/types
                   disj ::authority/election))))
    (testing "false with just election type"
      (is (not (util/registration-authority?
                (update test-authority ::authority/types
                        disj ::authority/registration)))))
    (testing "false with empty types"
      (is (not (util/registration-authority? {::authority/types #{}}))))
    (testing "false with no types keyword"
      (is (not (util/registration-authority? {}))))))

(deftest election-authority?-test
  (let [test-authority {::authority/types #{::authority/registration
                                            ::authority/election}}]
    (testing "true with both types"
      (is (util/election-authority? test-authority)))
    (testing "true with just election type"
      (is (util/election-authority?
           (update test-authority ::authority/types
                   disj ::authority/registration))))
    (testing "false with just registration type"
      (is (not (util/election-authority?
                (update test-authority ::authority/types
                        disj ::authority/election)))))
    (testing "false with empty types"
      (is (not (util/election-authority? {::authority/types #{}}))))
    (testing "false with no types keyword"
      (is (not (util/election-authority? {}))))))
