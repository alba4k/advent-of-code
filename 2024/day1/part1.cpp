// https://adventofcode.com/2024/day/1

#include <iostream>
#include <string>
#include <cmath>

#include "inputs.hpp"

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
    std::vector<int> inputs1;
    std::vector<int> inputs2;

    for(std::array<int, 2> i : inputs) {
        inputs1.push_back(i[0]);
        inputs2.push_back(i[1]);
    }

    sort(inputs1);
    sort(inputs2);

    int distance = 0;

    for(int i = 0; i < inputs1.size(); ++i)
        distance += abs(inputs1[i] - inputs2[i]);

    std::cout << "The total distance is " << distance << std::endl;

    return 0;
}
