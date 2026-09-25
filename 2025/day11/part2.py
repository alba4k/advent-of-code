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

def countSTPaths(alist: list[list[int]], s: int, t: int, memo: dict):
    if s == t: return 1
    if memo[s + t] != -1: return memo[s + t]

    sum = 0
    for n in alist[s]:
        sum += countSTPaths(alist, n, t, memo)
    memo[s + t] = sum
    return sum

alist = parseInput("input")
alist["out"] = []

memo:dict = {}
for key1 in alist.keys():
    for key2 in alist.keys():
        memo[key1 + key2] = -1

svr_dac = countSTPaths(alist, "svr", "dac", memo)
dac_fft = countSTPaths(alist, "dac", "fft", memo)
fft_out = countSTPaths(alist, "fft", "out", memo)

svr_fft = countSTPaths(alist, "svr", "fft", memo)
fft_dac = countSTPaths(alist, "fft", "dac", memo)
dac_out = countSTPaths(alist, "dac", "out", memo)

print(svr_dac*dac_fft*fft_out + svr_fft*fft_dac*dac_out)
