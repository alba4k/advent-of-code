#include <fstream>
#include <iostream>
#include <string>
#include <vector>

std::vector<std::string> readToVector(const std::string& filename) {
    std::vector<std::string> lines;
    std::ifstream file(filename);
    if(!file.is_open()) return {};

    std::string line;
    while (std::getline(file, line))
        lines.push_back(line);

    return lines;
}


int main() {
    int count = 0;
    std::vector<std::string> input = readToVector("input");
    
    for(int i = 0; i < std::size(input); i++) {
        for(int j = 0; j < input[i].length(); j++) {
            if(input[i][j] != '@') continue;

            int paper_count = 0;
            int max_i = std::size(input)-1;
            int max_j = input[i].length()-1;

            if(i > 0     && j > 0     && input[i-1][j-1] == '@') ++ paper_count; 
            if(i > 0                  && input[i-1][j]   == '@') ++ paper_count; 
            if(i > 0     && j < max_j && input[i-1][j+1] == '@') ++ paper_count; 
            if(             j > 0     && input[i][j-1]   == '@') ++ paper_count; 
            if(             j < max_j && input[i][j+1]   == '@') ++ paper_count; 
            if(i < max_i && j > 0     && input[i+1][j-1] == '@') ++ paper_count; 
            if(i < max_i              && input[i+1][j]   == '@') ++ paper_count; 
            if(i < max_i && j < max_j && input[i+1][j+1] == '@') ++ paper_count; 

            if(paper_count < 4) ++count;
        }
    }

    std::cout << count << std::endl;
}
