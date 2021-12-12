(ns advent.advent)

(comment)                                                   ;; https://adventofcode.com/2021/day/6


(def fun [2 3 2 0 1])


(def testdata [5 4 3 5 1 1 2 1 2 1 3 2 3 4 5 1 2 4 3 2 5 1 4 2 1 1 2 5 4 4 4 1 5 4 5 2 1 2 5 5 4 1 3 1 4 2 4 2 5 1 3 5 3 2 3 1 1 4 5 2 4 3 1 5 5 1 3 1 3 2 2 4 1 3 4 3 3 4 1 3 4 3 4 5 2 1 1 1 4 5 5 1 1 3 2 4 1 2 2 2 4 1 2 5 5 1 4 5 2 4 2 1 5 4 1 3 4 1 2 3 1 5 1 3 4 5 4 1 4 3 3 3 5 5 1 1 5 1 5 5 1 5 2 1 5 1 2 3 5 5 1 3 3 1 5 3 4 3 4 3 2 5 2 1 2 5 1 1 1 1 5 1 1 4 3 3 5 1 1 1 4 4 1 3 3 5 5 4 3 2 1 2 2 3 4 1 5 4 3 1 1 5 1 4 2 3 2 2 3 4 1 3 4 1 4 3 4 3 1 3 3 1 1 4 1 1 1 4 5 3 1 1 2 5 2 5 1 5 3 3 1 3 5 5 1 5 4 3 1 5 1 1 5 5 1 1 2 5 5 5 1 1 3 2 2 3 4 5 5 2 5 4 2 1 5 1 4 4 5 4 4 1 2 1 1 2 3 5 5 1 3 1 4 2 3 3 1 4 1 1])


(defn spawn  [msg]  (if (< msg 1) [6 :child 8] [(dec msg)]))



(def condj ((remove nil?) conj))


(defn spawncat  [[parents children] [parent & {:keys [child] :or {child nil} }]]
  [(conj parents parent) (condj children child)])

(defn spawngen [generation]
  (reduce concat (reduce spawncat [[] []] (map spawn generation))))

(count (nth (iterate spawngen testdata) 80))

(comment)                                                   ;; pt. 1 answer was 350917. https://adventofcode.com/2021/day/6#part2 :

(count (nth (iterate spawngen initdata) 256))             ;; too slow!

(defn spawnreducer [children msg]  (if (< msg 1) ([6 (children) 8] [(dec msg)]))

(map spawn generation)

(defn spawn  [msg]  (if (< msg 1) [6 :child 8] [(dec msg)]))


(defn spawngen [generation]
  ( let [[children parents]] (reduce spawncat [[] []] (map spawn generation))

                             )
  )
