#!/bin/fish

# input should be in input.txt

set number 50
set count 0

while read -l line
    switch (string sub -l 1 -- $line)
        case R
            set -l moves (string sub -s 2 -- $line)
            set number (math "$number + $moves")
            while test $number -gt 99
                set count (math "$count + 1")
                set number (math "$number - 100")
            end
        case L
            set -l moves (string sub -s 2 -- $line)
            for i in (seq $moves) # LMAO
                set number (math "(($number - 1) % 100 + 100) % 100") # the +100 takes care of negatives
                if test $number = 0
                    set count (math "$count + 1")
                end
            end
        case '*'
            exit 1
    end
end < input.txt

echo $count