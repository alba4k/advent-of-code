#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Range {
    long min;
    long max;
};

int main() {
    FILE *fp = fopen("input", "r");

    if(fp == NULL) {
        fputs("No such file", stderr);
        return 1;
    }

    fseek(fp, 0, SEEK_END);
    size_t len = (size_t)ftell(fp);
    fseek(fp, 0, SEEK_SET);

    char *input = malloc(len);
    if(input == NULL) {
        perror("malloc");
        return -1;
    }
    size_t read = fread(input, sizeof(*input), len, fp);
    fclose(fp);
    if(read > 0)
        input[read - 1] = 0;
    else {
        fprintf(stderr, "fread() failed: %zu\n", read);
        return 2;
    }

    char *ranges_str = input;
    char *values_str = strstr(input, "\n\n") + 2;
    if(!values_str) return -1;
    values_str[-2] = 0;

    int ranges_count = 1;
    int values_count = 1;
    char *ptr = ranges_str;
    while((ptr = strchr(ptr, '\n'))) {ranges_count++; ptr++;}
    ptr = values_str;
    while((ptr = strchr(ptr, '\n'))) {values_count++; ptr++;}

    struct Range ranges[ranges_count];
    long values[values_count];

    ptr = ranges_str;
    for(int i = 0; i < ranges_count; i++) {
        char *end = strchr(ptr, '\n');
        if(end) *end = 0;
        char *start1 = ptr;
        char *mid = strchr(start1, '-');
        *mid = 0; // unchecked but stfu
        char *start2 = mid+1;
        ranges[i].min = atol(start1);
        ranges[i].max = atol(start2);
        ptr = end+1;
    }

    ptr = values_str;
    for(int i = 0; i < values_count; i++) {
        char *end = strchr(ptr, '\n');
        if(end) *end = 0;
        values[i] = atol(ptr);
        ptr = end+1;
    }

    int inside = 0;
    for(int i = 0; i < values_count; i++) {
        for(int j = 0; j < ranges_count; j++)
            if(values[i] >= ranges[j].min && values[i] <= ranges[j].max) {
                inside++;
                break;
            }
    }

    printf("%d\n", inside);

    return 0;
}
