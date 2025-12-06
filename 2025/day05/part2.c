#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    long min;
    long max;
} Range;

int compareRanges(const void *a, const void *b) {
    Range r1 = *(Range *)a;
    Range r2 = *(Range *)b;

    if(r1.min < r2.min) return -1;
    if(r1.min > r2.min) return 1;
    if(r1.max < r2.max) return -1;
    if(r1.max > r2.max) return 1;
    return 0;
}

size_t mergeRanges(Range *ranges, size_t count) {
    if(count == 0) return 0;

    qsort(ranges, count, sizeof(Range), compareRanges);

    Range *merged_ranges = malloc(count*sizeof(Range));
    if(merged_ranges == NULL)
        exit(1);
    
    merged_ranges[0] = ranges[0];
    size_t merged_count = 1;

    for(size_t i = 1; i < count; i++) {
        //                  &merged_ranges[merged_count-1];
        Range *lastMerged = merged_ranges + merged_count - 1;
        Range current = ranges[i];

        if(current.min <= lastMerged->max + 1) {
            if(current.max > lastMerged->max)
                lastMerged->max = current.max;
        } else
            merged_ranges[merged_count++] = current;
    }

    memcpy(ranges, merged_ranges, merged_count*sizeof(Range));
    free(merged_ranges);

    return merged_count;
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
    if(input == NULL)
        return -1;

    size_t read = fread(input, sizeof(*input), len, fp);
    fclose(fp);
    if(read > 0)
        input[read - 1] = 0;
    else
        return 2;

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
        char *max = strchr(ptr, '\n');
        if(max) *max = 0;
        char *min1 = ptr;
        char *mid = strchr(min1, '-');
        *mid = 0; // unchecked but stfu
        char *min2 = mid+1;
        ranges[i].min = atol(min1);
        ranges[i].max = atol(min2);
        ptr = max+1;
    }

    size_t merged_count = mergeRanges(ranges, ranges_count);

    long total = 0;
    for(size_t i = 0; i < merged_count; i++)
        total += ranges[i].max - ranges[i].min + 1;
    
    printf("%ld\n", total);

    return 0;
}
