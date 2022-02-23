(ns advent.day-1
  (:require (clojure [set :as set] [string :as str]) :verbose
            [clojure.test :refer [is deftest testing]]
            [clojure.tools [trace :as trace]]))

;; https://adventofcode.com/2021/day/1

(def puzzle-input (str/split (slurp "./src/advent/day_1.in.txt") #"\n"))

(def example-input ["199" "200" "208" "210" "200" "207" "240" "269" "260" "263"])

(defn count-increases [input]
  (->> (partition 2 1 input)
       (map (partial apply -))
       (filter (partial > 0))
       (count)))


(defn part1 [input]
(->> (map (partial read-string) input)
     (count-increases)
     ))

(is (= 7 (part1 example-input)))

(is (= 1482 (part1 puzzle-input)))

;; part 2


(defn three-measurement-sums [input]
  (->> (partition 3 1 input)
       (map (comp (partial apply +) (partial map read-string))
       )))

(is (= [607 618 618 617 647 716 769 792] (three-measurement-sums example-input)))

(count-increases (three-measurement-sums example-input))

(is (= 5 (count-increases (three-measurement-sums example-input))))

(count-increases (three-measurement-sums puzzle-input))

(is (= 1518 (count-increases (three-measurement-sums puzzle-input))))
