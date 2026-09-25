#!/bin/python

def parseInput(file: str) -> dict:
    with open(file, "r") as f:
        input = f.read()

    lines = input.split("\n")
    adj_list: dict = {}
    for line in lines:
        tokens = line.split(" ")
        adj_list[tokens[0][:-1]] = tokens[1:]

    return adj_list

def countSTPaths(alist: list[list[int]], s: int, t: int):
    # output is small, no memo needed
    if s == t: return 1

    sum = 0
    for n in alist[s]:
        sum += countSTPaths(alist, n, t)
    return sum

alist = parseInput("input")

print(countSTPaths(alist, "you", "out"))
