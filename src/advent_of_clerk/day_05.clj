;; # 🎄 Advent of Clerk: Day 5
(ns advent-of-clerk.day-05
  (:require [nextjournal.clerk :as clerk]
            [clojure.string :as string]))

;; ## Parsing the input
;; Example:
(def input "    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2")

(comment (def input (slurp "input/2022/05")))

;;
;; The stacks description is a number of lines, each of the same length $$l$$.
;; The number of stacks $$n$$ can be found like this:
;; $$l = 3 \times n + (n-1) = 4 \times n - 1; n = \frac{l+1}{4}$$.
;;
;; Parse the stacks as vectors — left to right, top to bottom:
;;
(defn parse-stacks [s]
  (let [lines (string/split-lines s)
        depth (dec (count lines))
        l     (count (first lines))
        n     (/ (+ l 1) 4)]
    (for [i (range n)
          :let [col (+ 1 (* 4 i))]]
      (for [j (range depth)
            :let [line (nth lines j)
                  c    (nth line col)]
            :when (not= c \ )]
        c))))

;;
;; Print the stacks in the same format as we got them on input.  We don't need
;; it to solve the puzzle, but it's helpful for debugging:
;;
(defn print-stacks [sts]
  (let [n     (count sts)
        depth (->> sts (map count) (apply max))]
    (doseq [j (range depth)]
      (println (string/join " "
                            (for [st sts
                                  :let [len (count st)
                                        k   (- j (- depth len))]]
                              (if-not (neg? k)
                                (format "[%c]" (nth st k))
                                "   ")))))
    ;; add stack numbers at the bottom:
    (println (string/join " " (map #(format " %d " (inc %))
                                   (range n))))))

(defn parse-moves [s]
  (let [lines (string/split-lines s)]
    (for [l lines
          :let [[_ n f t] (re-find #"^move ([0-9]+) from ([0-9]+) to ([0-9]+)$" l)]]
      {:count n
       :from  f
       :to    t})))

(defn parse [s]
  (let [[stack-lines move-lines] (string/split s #"\n\n")
        stacks (parse-stacks stack-lines)]
    ;; safety net for parse/print round-trip:
    (assert (= (str stack-lines "\n")
               (with-out-str (print-stacks stacks))))
    {:stacks stacks
     :moves  (parse-moves move-lines)}))

(def puzzle (parse input))

(with-out-str
  (print-stacks (:stacks puzzle)))
