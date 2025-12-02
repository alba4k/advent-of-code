# https://adventofcode.com/2024/day/5

from input import rules, updates

sum: int = 0
correct_order: bool = True

def find_index(n: int, list: list[int]):
    for i in range(0, len(list)):
        if list[i] == n:
            return i
    return None

for update in updates:
    correct_order = True
    for page in update:
        for rule in rules:
            if(rule[0] == page):
                i = find_index(rule[1], update)
                j = find_index(page, update)
                if i != None and i < j:
                    pass

print("The final sum of page numbers is", sum)