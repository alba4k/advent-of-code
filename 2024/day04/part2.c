// https://adventofcode.com/2024/day/4#part2

#include <stdio.h>

#include "input.h"

// just brute forced it
int main() {
    int count = 0;

    /*
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
    */
    for(int x = 0; x < SIZE-2; ++x)
        for(int y = 0; y < SIZE-2; ++y) {
            if((((input[y][x] == 'M' && input[y+2][x] == 'M') && (input[y][x+2] == 'S' && input[y+2][x+2] == 'S'))
             || ((input[y][x] == 'S' && input[y+2][x] == 'S') && (input[y][x+2] == 'M' && input[y+2][x+2] == 'M'))
             || ((input[y][x] == 'S' && input[y][x+2] == 'S') && (input[y+2][x] == 'M' && input[y+2][x+2] == 'M'))
             || ((input[y][x] == 'M' && input[y][x+2] == 'M') && (input[y+2][x] == 'S' && input[y+2][x+2] == 'S')))
             &&  (input[y+1][x+1] == 'A'))
                ++count;
        }
        
    printf("The X-MAS count is %d\n", count);
    
    return 0;
}
