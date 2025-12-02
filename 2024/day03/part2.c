// https://adventofcode.com/2024/day/3#part2

#include <math.h>
#include <stdio.h>
#include <string.h>

#include "input.h"

// only search the next n characters of __s
char *strnchr(char *__s, int __c, int n) {
    char tmp = __s[n];
    __s[n] = 0;
    char *ptr = strchr(__s, __c);
    __s[n] = tmp;
    return ptr;
}

int my_atoi(char *str) {
    size_t len = strlen(str);
    int n = 0;
    
    for(int i = 0; i < len; ++i) {
        if(str[i] > '9' || str[i] < '0')
            return -1;
        n += pow(10, (len-1)-i) * (str[i] - '0');
    }

    return n;
}

int run_sum(char *str) {
    char *start = str, *end;
    int n1, n2, result = 0;

    while((start = strstr(start, "mul("))) {
        start += 4;
        end = strnchr(start, ',', 4);
        if(!end)
            continue;
        *end = 0;

        n1 = my_atoi(start);

        if(n1 == -1 || n1 > 999) {
            start = end+1;
            continue;
        }

        start = end+1;
        end = strnchr(start, ')', 4);
        if(!end)
            continue;
        *end = 0;

        n2 = my_atoi(start);

        if(n2 == -1 || n2 > 999) {
            start = end+1;
            continue;
        }

        result += n1*n2;

        start = end+1;
    }

    return result;
}

int main() {
    char *start = input, *end;
    int result = 0;

    do {
        end = strstr(start, "don't()");
        if(end)
            *end = 0;
            
        result += run_sum(start);

        if(end)
            start = end+1;
        else
            break;
    } while((start = strstr(start, "do()")));

    printf("The result is %d\n", result);

    return 0;
}
