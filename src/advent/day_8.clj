(ns advent.day-8)
(require '[clojure.string :as str])
(require '[clojure.set :as set])


;; parse file
(slurp "src/advent/day_8.in.txt")

(def testdata (slurp "src/advent/day_8.in.txt"))

(def output-values
  (take-nth 2 (drop 1 (str/split testdata #"\n"))))

;; constant for digits
(def digit-signal-count {0 6, 1 2, 2 5, 3 5, 4 4, 5 5, 6 6, 7 3, 8 7, 9 6})

;; (def signal-count-digit (set/map-invert digit-signal-count))

;(defn map-over-values-in-map [m f]
;  (into {} (for [[k v] m] [k (f v)])))

(def unique-signals
  (map flatten (map second
       (filter #(= 1 (count (second %))) (group-by #(second %) digit-signal-count)))))

(def unique-signal-counts (map second unique-signals))

(def output-value-lengths
  (map count (flatten (map #(str/split % #" ") output-values))))

; only show value counts we care about
(def output-value-length-freq (frequencies output-value-lengths))

(def count-of-matches
  (reduce + (map #(get output-value-length-freq %) unique-signal-counts)))

;;; todo count puzzle input for pt 1