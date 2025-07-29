// https://adventofcode.com/2024/day/1#part2

#include <iostream>

#include "inputs.hpp"

int appearances(std::vector<int> v, int n) {
    int N = 0;
    for(int i : v)
        if(i == n)
            ++N;
    return N;
}

int main() {
    std::vector<int> inputs1;
    std::vector<int> inputs2;

    for(std::array<int, 2> i : inputs) {
        inputs1.push_back(i[0]);
        inputs2.push_back(i[1]);
    }

    int similarity = 0;

    for(int i : inputs1)
        similarity += i*appearances(inputs2, i);

    std::cout << "The total similarity is " << similarity << std::endl;

    return 0;
}
