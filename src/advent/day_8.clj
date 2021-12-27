(ns advent.day-8)
(require '[clojure.string :as str])
(require '[clojure.set :as set])


;; parse file
(slurp "src/advent/day_8.in.txt")

(def testdata (slurp "src/advent/day_8.in.txt"))


(defn splitdata [data]  (str/split data #"\s*\|\s*|\s*\n\s*"))

(defn input-values [data]
  (take-nth 2 (splitdata data)))

(defn output-values [data]
  (map #(str/split % #" ") (take-nth 2 (drop 1 (splitdata testdata)))))
;  (str/split (first (take-nth 2 (drop 1 (splitdata data)))) #" ")



(defn splitdata [data]
  (map #(str/split % #"\s*\|[\s\n]*")
       (str/split data #"\w*\n\s*")))


(defn input-values [data]
  (take-nth 2
            (map first (splitdata data))))

(defn output-values [data]
  (str/split data " "))

;; constant for digits
(def digit-signal-count {0 6, 1 2, 2 5, 3 5, 4 4, 5 5, 6 6, 7 3, 8 7, 9 6})

;; (def signal-count-digit (set/map-invert digit-signal-count))

;(defn map-over-values-in-map [m f]
;  (into {} (for [[k v] m] [k (f v)])))

(def unique-signals
  (map flatten (map second
       (filter #(= 1 (count (second %))) (group-by #(second %) digit-signal-count)))))

(def unique-signal-counts (set (map second unique-signals)))

(def output-value-lengths
  (map count (flatten (map #(str/split % #" ") (output-values testdata)))))

; only show value counts we care about
(def output-value-length-freq (frequencies output-value-lengths))

(def count-of-matches
  (reduce + (map #(get output-value-length-freq %) unique-signal-counts)))

;; 421

;; pt 2

(def signal-count-digits (group-by #(second %) digit-signal-count))

;; the 'trick' is to spot that 'strictly contained in' gets you everything

;; 1 is only contained in one of [2,3,5] (code-length 5) ie. 3
;; so now we know 1,3,4,7,8
;; 1 is not contained in one of [0,6,9] (code-length 6) ie. 6
;; so now we know 1,3,4,6,7,8
;; only 5 (for code-length 5) is contained in 6
;; so now we know 1,3,4,5,6,7,8
;; two is whatever is left undiscovered of code-length 5
;; so now we know 1,2,3,4,5,6,7,8
;; just 0 and 9 left, only 3 contained in 9 (for code-lengths 6)

  (def example "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |\ncdfeb fcadb cdfeb cdbaf")

;; get simple 1,4,7,8 codes
(defn simple-codes  [collection]
  (for
    [i (filter #(contains? unique-signal-counts (count %)) collection)]
    [(first (flatten (signal-count-digits (count i)))) (set i)]))

;(simple-codes example-input-values)
;=> ([8 #{\a \b \c \d \e \f \g}] [7 #{\a \b \d}] [4 #{\a \b \e \f}] [1 #{\a \b}])

(defn find-three [collection]
  (set (first (filter #(and
             (= 5 (count %))
             (set/subset? (get (simple-codes collection) 1) (set %)))
          collection))))

(find-three example-input-values)
;=> ("fbcad")

(defn find-six [collection]
  (set (first (filter #(and
             (= 6 (count %))
             (not (set/subset? (get (simple-codes collection) 1) (set %))))
          collection))))

(find-six example-input-values)
;=> ("cdfgeb")

(defn find-five [collection]
  (set (first (filter #(and
           (= 5 (count %))
           (set/subset? (set %) (set (find-six collection))))
        collection))))

(find-five example-input-values)
;=> ("cdfbe")

; now we can find two


; find codes left over after two other codes found in triplet
(defn leftover-code [collection code-length code1 code2]
  (set (first (filter #(and
             (= code-length (count %))
             (not (set/subset? (set %) code1))
             (not (set/subset? (set %) code2))
             ) collection))))



(defn find-two [collection]
  (leftover-code
    collection
    5
    (find-three collection)
    (find-five collection)))

(find-two example-input-values)
;=> ("gcdfa")


(defn find-nine [collection]
  (set (first (filter #(and
                    (= 6 (count %))
                    (set/subset? (find-three collection) (set %)))
                 collection))))
(find-nine example-input-values
           )


;=> ("cefabd")

(defn find-zero [collection]
  (leftover-code
    collection
    6
    (find-six collection)
    (find-nine collection)))
;=> ("cagedb")

(defn get-codes [collection]
  (hash-map
    (find-zero collection) 0,
    (get (simple-codes collection) 1) 1,
    (find-two collection) 2,
    (find-three collection) 3,
    (get (simple-codes collection) 4) 4,
    (find-five collection) 5,
    (find-six collection) 6,
    (get (simple-codes collection) 7) 7,
    (get (simple-codes collection) 8) 8,
    (find-nine collection) 9
      ))


; now we can decode the output :
(map #(get (get-codes (str/split (first (input-values example)) #" ")) (set %))
     (output-values example))
;=> (5 3 5 3)

; now we need to extend to all the rest

(def split-test-data
  (partition 2 (map #(str/split % #" ") (splitdata testdata))))

; just see examples decoded
(map (comp get-codes first) split-test-data)

; decode each example and look up the answers
(def test-answers (map
  #(map
     (comp (partial get (get-codes (first %))) set)
     (second %)) split-test-data))

(defn get-number [output-number]
  (Integer/parseInt (apply str output-number)))


