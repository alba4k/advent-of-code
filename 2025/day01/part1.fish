#!/bin/fish

# input should be in input.txt

set number 50
set count 0

while read -l line
    echo $line
    switch (string sub -l 1 -- $line)
        case R
            set -l move (string sub -s 2 -- $line)
            set number (math "($number + $move) % 100")
        case L
            set -l move (string sub -s 2 -- $line)
            set number (math "(($number - $move) % 100 + 100) % 100") # the +100 takes care of negatives
        case '*'
            exit 1
    end
    
    if test $number = 0
        set count (math "$count + 1")
    end
    echo $number
end < input

echo $count