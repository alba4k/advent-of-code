#!/bin/python
from math import floor, sqrt

def divisors(number: str) -> list[int]:
    result: list[int] = []
    for i in range(1, number):
        if number/i == float(number//i):
            result.append(i)
    return result

def repeatsPattern(number: str) -> bool:
    for length in divisors(len(number)):
        for i in range(1, len(number)//length):
            allEqual: bool = True
            if(number[i*length:(i+1)*length] != number[:length]):
                allEqual = False
                break
                
        if(allEqual):
            return True
    return False
    
    
sum: int = 0

with open("input", "r") as file:
    input: str = file.read()

ranges: list[str] = input.split(",")

for id_range_str in ranges:
    id_range: list[str] = id_range_str.split("-")

    for i in range(int(id_range[0]), int(id_range[1])+1):
        if repeatsPattern(str(i)):
            sum += i

print(sum)
