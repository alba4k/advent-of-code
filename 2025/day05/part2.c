#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    long min;
    long max;
} Range;

long max(long a, long b) {
    return (a >= b)*a + (b > a)*b;
}
long min(long a, long b) {
    return (a <= b)*a + (b > a)*b;
}

Range intersection(Range a, Range b) {
    long max_start = max(a.min, b.min);
    long min_end = min(a.max, b.max);
    if(max_start > min_end) {
        max_start = -1;
        min_end = -1;
    }

    Range result = {
        min: max_start,
        max: min_end
       };
    return result;
}

bool isInRange(long n, Range r) {
    return r.min <= n && n <= r.max;
}

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
    char *ptr = strstr(input, "\n\n");
    if(!ptr) return -1;
    *ptr = 0;

    int ranges_count = 1;
    ptr = ranges_str;
    while((ptr = strchr(ptr, '\n'))) {ranges_count++; ptr++;}

    Range ranges[ranges_count];

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

    long count = 2;
    // remove duplicates
    for(int i = 0; i < ranges_count; i++) {
        for(int j = i+1; j < ranges_count; j++) { // this could be done better but who cares
            if(ranges[j].min == -1) continue;
            Range in = intersection(ranges[i], ranges[j]);
            if(in.min == -1) continue;

            if(isInRange(ranges[i].min, in)) {
                
            };
            if(isInRange(ranges[j].min, in));
            if(isInRange(ranges[i].max, in));
            if(isInRange(ranges[j].max, in));
        }
    }

    for(int i = 0; i < ranges_count; i++) {
        printf("%ld - %ld\n", ranges[i].min, ranges[i].max);
        if(ranges[i].max != -1) count += (ranges[i].max - ranges[i].min) + 1;
    }

    printf("%ld\n", count);

    return 0;
}
