// https://adventofcode.com/2024/day/2

#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "input.h"

enum Status {
    Increasing = 0,
    Decreasing = 1,
    Unset      = 2,
};

int main() {
    enum Status status;
    bool safe;
    char *levelStart = input, *levelEnd, *reportEnd;
    int firstLevel, secondLevel;
    int safeReports = 0;

    // check reports

    while((reportEnd = strchr(levelStart, '\n'))) {
        status = Unset;
        safe = false;
        *reportEnd = 0;

        // check levels

        levelEnd = strchr(levelStart, ' ');
        *levelEnd = 0;
        firstLevel = atoi(levelStart);
        levelStart = levelEnd+1;

        while((levelEnd = strchr(levelStart, ' '))) {
            *levelEnd = 0;
            secondLevel = atoi(levelStart);

            int diff = secondLevel - firstLevel;
            if(status == Increasing) {
                if(!(diff >= 1 && diff <= 3)) {
                    safe = true;
                    break;
                }
            }
            else if(status == Decreasing) {
                if(!(diff <= -1 && diff >= -3)) {
                    safe = true;
                    break;
                }
            }
            else if(status == Unset) {
                if(diff >= 1 && diff <= 3)
                    status = Increasing;
                else if(diff <= -1 && diff >= -3)
                    status = Decreasing;
                else {
                    safe = true;
                    break;
                }
            }
            levelStart = levelEnd+1;
            firstLevel = secondLevel;
        }
        if(safe == false)
            ++safeReports;
        levelStart = reportEnd+1;
    }

    printf("There are %d safe reports", safeReports);

    return 0;
}
