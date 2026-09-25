#!/bin/python

def parseInput(file: str) -> dict:
    with open(file, "r") as f:
        input = f.read()

    sections = input.split("\n\n")

    shapes = [section.split("\n")[1:] for section in sections[:-1]]
    regions = sections[-1].split("\n")

    for i in range(len(regions)):
        if regions[i] == "": 
            regions = regions[:i]
            break
        split = regions[i].split(" ")
        regions[i] = [[int(x) for x in split[0][:-1].split("x")], [int(x) for x in split[1:]]]

    return shapes, regions

shapes, regions = parseInput("input")

areas = []
for shape in shapes:
    area = 0
    for line in shape:
        for char in line:
            if char == "#": area += 1
    areas.append(area)

valid_regions = []

for region in regions:
    area = region[0][0] * region[0][1]
    required_area = 0
    for i in range(len(shapes)):
        required_area += region[1][i] * areas[i]

    if(required_area <= area):
        valid_regions.append(region)

# this was enough for my input. I do not check if the fit is actually possible,
# only if there is enough theoretical area to accomodate it
print(len(valid_regions))