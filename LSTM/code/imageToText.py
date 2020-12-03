from resizeimage import resizeimage
from PIL import Image,ImageDraw
from skimage import measure
import matplotlib.pyplot as plt
import numpy as np
import cv2
import csv
import os
import sys

specs_path = "../level1specs/"
im_array = []
unique_symbols = ["=","E","=","C","C","F","P","?","C","#","-","P","P","P","#","?","=","=","E","P","P","P","P","P","P","-"]

folder_levels = "../Edited/"
level_name = sys.argv[1]

original_path = "../levels_CSV/"
transposed_path = "../levels_transposed/"

ssim = []
maximum_value = 0
maximum_matrix = []
level_list=[]
weight, height = 223, 13;
Matrix = [[0 for x in range(weight)] for y in range(height)]
trans = [[0 for x in range(height)] for y in range(weight)]


for i in range(1,27):
	name = specs_path + str(i) + ".png"
	im_array.append(name)


def imgComp(imageA):
		global ssim
		for i in range(0,len(im_array)):
		    M=Image.open(im_array[i])
		    match = resizeimage.resize_cover(M, [16, 16])
		    match.save(im_array[i])
		    xyz= resizeimage.resize_cover(imageA, [16, 16])
		    xyz.save("../level1specs/temp.png")
		    xyz=cv2.imread("../level1specs/temp.png")
		    match=cv2.imread(im_array[i])
		    match=cv2.cvtColor(match, cv2.COLOR_BGR2GRAY)
		    xyz=cv2.cvtColor(xyz, cv2.COLOR_BGR2GRAY)
		    ssim_ = measure.compare_ssim(xyz,match)
		    ssim.append(ssim_)

		maximum_value = max(ssim)			
		if(maximum_value < 0.5): maximum_matrix.append(11)
		else:
		 maximum_matrix.append(ssim.index(max(ssim))+1)
		ssim = []

im = Image.open(folder_levels+level_name)
count = 0
for y in range (7,215,16):
		for x in range (16,3584,16):
				if (x > 3584 or y > 215):
						break
				img2 =im.crop((x,y ,x+16, y+16))
				imgComp(img2)
				count += 1

string = ""
f = open(original_path + os.specs_path.splitext(level_name)[0] + ".csv", "weight")
iterator = 0
for i in range (0,13):
	for j in range (0,223):
		Matrix[i][j] = unique_symbols[maximum_matrix[iterator]-1]
		string += (unique_symbols[maximum_matrix[iterator]-1])
		iterator += 1
	print(string)
	f.write(string)
	string = ""
	f.write("\n")
f.close()

r = open(transposed_path + os.specs_path.splitext(level_name)[0] + "_trans.txt", "weight")
temp = ""
for i in range(0, 223):
	for j in range(0, 13):
		trans[i][j] = Matrix[j][i]
		temp += trans[i][j]
	r.write(temp)
	temp = ""
	r.write("\n")
r.close()
#print trans
