#!/bin/python

with open("input", "r") as file:
    input: list[str] = file.readlines()

numbers: list[list[str]] = []

for line in input:
    numbers.append(line.split())

total: int = 0

for y in range(len(numbers[0])):
    current: int = 0
    if numbers[4][y] == "*":
        current = 1
        for x in range(4):
            current *= int(numbers[x][y])
    elif numbers[4][y] == "+":
        current = 0
        for x in range(4):
            current += int(numbers[x][y])
    total += current

print(total)

