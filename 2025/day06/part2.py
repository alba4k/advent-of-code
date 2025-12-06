#!/bin/python

with open("input", "r") as file:
    input: list[str] = file.readlines()

total: int = 0
operation: str = ""

current: int = 0
for y in range(len(input[0])):
    if input[0][y] == "\n":
        total += current
        break
    if input[4][y] == "+":
        operation = "+"
        current = 0
    if input[4][y] == "*":
        operation = "*"
        current = 1
    if input[0][y] == input[1][y] == input[2][y] == input[3][y] == input[4][y] == " ":
        total += current
        continue

    num: int = 0
    if input[0][y] != " ":
        n: int = int(input[0][y])
        if input[3][y] != " ":
            n *= 1000
        elif input[2][y] != " ":
            n *= 100
        elif input[1][y] != " ":
            n *= 10
        num += n
    if input[1][y] != " ":
        n: int = int(input[1][y])
        if input[3][y] != " ":
            n *= 100
        elif input[2][y] != " ":
            n *= 10
        num += n
    if input[2][y] != " " :
        n: int = int(input[2][y])
        if input[3][y] != " ":
            n *= 10
        num += n
    if input[3][y] != " ":
        n: int = int(input[3][y])
        num += n

    if operation == "+":
        current += num
    elif operation == "*":
        current *= num

print(total)
