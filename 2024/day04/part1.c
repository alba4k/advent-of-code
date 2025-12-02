// https://adventofcode.com/2024/day/4

#include <stdbool.h>
#include <stdio.h>
#include <string.h>

#include "input.h"

int count_in_line(char *str, char *word) {
    int count = 0;
    char *start = str;
    while((start = strstr(start, word))) {
        ++count;
        start += strlen(word);
    }

    return count;
}

void transpose(char src[SIZE][SIZE+1], char dest[SIZE][SIZE+1]) {
    for(int i = 0; i < SIZE; ++i) {
        for(int j = 0; j < SIZE; ++j)
            dest[i][j] = src[j][i];
        dest[i][SIZE] = 0;
    }
}

// I hated this part.
void diagonalize(bool dir, char src[SIZE][SIZE+1], char dest[2*SIZE-1][SIZE+1]) {
    memset(dest, 0, (SIZE+1)*(2*SIZE-1));

    for(int i = 0; i < SIZE; ++i)                   // i: 0 -> 139
        for(int j = 0; j <= i; ++j)                 // j: 0 -> [0 -> 139]
            dest[i][j] = src[j][dir?SIZE-1-i+j:i-j];
    for(int i = SIZE; i < 2*SIZE-1; ++i)            // i: 140 -> 278
        for(int j = 0; j <= 2*SIZE-2-i; ++j)        // j: 0 -> [138 -> 0]
            dest[i][j] = src[j+i+1-SIZE][dir?j:SIZE-1-j];
}

int main() {
    int total = 0;
    
    // lines
    for(int i = 0; i < SIZE; ++i)
        total += count_in_line(input[i], "XMAS");
    for(int i = 0; i < SIZE; ++i)
        total += count_in_line(input[i], "SAMX");

    // columns
    {
        char transposed[SIZE][SIZE+1];
        transpose(input, transposed);
        for(int i = 0; i < SIZE; ++i)
            total += count_in_line(transposed[i], "XMAS");
        for(int i = 0; i < SIZE; ++i)
            total += count_in_line(transposed[i], "SAMX");
    }

    // diag /
    char diagonalized[2*SIZE-1][SIZE+1];
    diagonalize(false, input, diagonalized);
    for(int i = 0; i < 2*SIZE-1; ++i)
        total += count_in_line(diagonalized[i], "XMAS");
    for(int i = 0; i < 2*SIZE-1; ++i)
        total += count_in_line(diagonalized[i], "SAMX");

    // diag \ 
    char diagonalized[2*SIZE-1][SIZE+1];
    diagonalize(true, input, diagonalized);
    for(int i = 0; i < 2*SIZE-1; ++i)
        total += count_in_line(diagonalized[i], "XMAS");
    for(int i = 0; i < 2*SIZE-1; ++i)
        total += count_in_line(diagonalized[i], "SAMX");

    printf("The word XMAS appears %d times\n", total);

    return 0;
}
