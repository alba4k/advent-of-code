# https://adventofcode.com/2024/day/4#part2

from input import input

count = 0

"""
    M.M
    .A.
    S.S
    
    M.S
    .A.
    M.S

    S.M
    .A.
    S.M
    
    S.S
    .A.
    M.M
"""


for x in range(138):
    for y in range(138):
        if((((input[y][x] == 'M' and input[y+2][x] == 'M') and (input[y][x+2] == 'S' and input[y+2][x+2] == 'S'))
         or ((input[y][x] == 'S' and input[y+2][x] == 'S') and (input[y][x+2] == 'M' and input[y+2][x+2] == 'M'))
         or ((input[y][x] == 'S' and input[y][x+2] == 'S') and (input[y+2][x] == 'M' and input[y+2][x+2] == 'M'))
         or ((input[y][x] == 'M' and input[y][x+2] == 'M') and (input[y+2][x] == 'S' and input[y+2][x+2] == 'S')))
         and (input[y+1][x+1] == 'A')):
            count += 1
        
print("The X-MAS count is", count);
    
