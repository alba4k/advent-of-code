#!/bin/python

def repeatsPattern(number: str) -> bool:
    m: int = len(number)//2
    return (number[:m] == number[m:])
    
sum: int = 0

with open("input", "r") as file:
    input: str = file.read()

ranges: list[str] = input.split(",")

for id_range_str in ranges:
    id_range: list[str] = id_range_str.split("-")
    print(id_range, id_range_str)

    for i in range(int(id_range[0]), int(id_range[1])+1):
        if repeatsPattern(str(i)):
            m: int = len(str(i))//2
            print(i, "repeats", str(i)[:m])
            sum += i

print(sum)

