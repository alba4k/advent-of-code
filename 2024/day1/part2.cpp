// https://adventofcode.com/2024/day/1#part2

#include <iostream>

#include "input.hpp"

int appearances(std::vector<int> v, int n) {
    int N = 0;
    for(int i : v)
        if(i == n)
            ++N;
    return N;
}

int main() {
    std::vector<int> input1;
    std::vector<int> input2;

    for(std::array<int, 2> i : input) {
        input1.push_back(i[0]);
        input2.push_back(i[1]);
    }

    int similarity = 0;

    for(int i : input1)
        similarity += i*appearances(input2, i);

    std::cout << "The total similarity is " << similarity << std::endl;

    return 0;
}
