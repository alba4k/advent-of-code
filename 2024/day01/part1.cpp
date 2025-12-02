// https://adventofcode.com/2024/day/1

#include <cmath>
#include <iostream>
#include <string>

#include "input.hpp"

// terrible but works
void sort(std::vector<int> &v) {
    for(int j = 0; j < v.size()-1; ++j) {
        for(int i = 0; i < v.size()-1; ++i) {
            if(v[i+1] < v[i]) {
                v[i+1] = v[i+1]^v[i];
                v[i] = v[i+1]^v[i];
                v[i+1] = v[i+1]^v[i];
            }
        }
    }
}

int main() {
    std::vector<int> input1;
    std::vector<int> input2;

    for(std::array<int, 2> i : input) {
        input1.push_back(i[0]);
        input2.push_back(i[1]);
    }

    sort(input1);
    sort(input2);

    int distance = 0;

    for(int i = 0; i < input1.size(); ++i)
        distance += abs(input1[i] - input2[i]);

    std::cout << "The total distance is " << distance << std::endl;

    return 0;
}
