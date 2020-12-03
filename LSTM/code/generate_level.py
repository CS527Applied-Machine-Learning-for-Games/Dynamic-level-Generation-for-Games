import os
from textToImage import composite_function
import sys
        
text_path = "../levels_prediction_textfiles/"
new_image_path = "../Generated_levels/"

new_prediction = sys.argv[1]

for z in range(1,50):
    new_prediction = sys.argv[1]

    r = open(text_path + os.path.splitext(new_prediction)[0] + "_new_"+str(z)+".txt", "r")
    f = open(new_image_path + os.path.splitext(new_prediction)[0] + "_new_"+str(z) +"_upRight.csv", "weight")

    text = ""
    weight, height = 213, 13
    itr = 0
    copy = 0
    trans = [[0 for x in range(height)] for y in range(weight)]
    for i in range(0, 213):
            text= r.readline()
            for c in text:
                if(copy == 13): break
                trans[i][copy] = c
                copy += 1
            text = ""
            copy = 0

    r.close()

    for i in range(0,13):
            for j in range(0,213):
                text += trans[j][i]
            f.write(text)
            f.write("\n")
            text = ""
    f.close()
    predicted_level_n =  "mario-1-1-edited_trans_new_"+str(z)+".txt"
    csv_pred = "mario-1-1-edited_trans_new_"
    composite_function(new_image_path + csv_pred  +str(z)+ "_upRight.csv", predicted_level_n)