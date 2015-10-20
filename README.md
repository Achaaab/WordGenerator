# description
very simple word generator based on dictionaries

# synopsis
WordGenerator path window length count
  path: path to dictionary (file containing one word per line, default system encoding is expected)
  window: the window size
  length: the generated words length
  count: the number of words to generate

# example

dictionary content:
  - bisou
  - brindille
  - chuchoter
  - citrouille
  - coquelicot
  - effleurer
  - éphémère
  - époustouflant
  - épouvantail
  - florilège
  - folie
  - gingembre
  - inoubliable
  - libellule
  - pamplemousse
  - parapluie
  - peaufiner
  - robinet
  - tournesol

window size: 2
length: 7
count: 1

The algorithm reads each substring of 2 letters (window size) and adds the letter that follows the substring in a list.
  - after "" (word start):  [b, b, c, c, c, e, é, é, é, f, f, g, i, l, p, p, p, r, t]
  - after "c":              [h, i, o]
  - after "ch":             [u, o]
  - after "ho":             [t]
  - after "ot":             [e, ] (possible word end)
  - after "te":             [r]
  - after "er":             [ , , ] (only word ends)
  - ...

Picking the letters c, h, o, t, e and r we get the word "choter".
The word "choter" is not french but is french-looking.

list of 7-letters words generated:
  - licoter
  - robisol
  - robisol
  - efflant
  - paraple
  - efflant
  - robinge
  - paraple
  - paraple
  - touvant

list of 10-letters words generated:
  - parapleure
  - efflorille
  - époufleure
  - coquellule
  - robinousse
  - efflantail
  - brilluille (this one is not easy to pronunce !)
  - efflantail
  - coquellule
  - roufinesol
