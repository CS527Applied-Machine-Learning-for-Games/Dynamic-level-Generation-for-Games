from PIL import Image,ImageDraw
import os

if __name__=="__main__":
    matrix_size = 3364,216
    change_string = ""
    width, heigth = 213, 13
    copy = 0
    newLevel_image = Image.new("RGBA", matrix_size, "white")
    path = "../level1specs/"
    newLevelPath = '../Generated_levels/'

    pictures = []
    symbols_array = ["=","e","=","c","c","f","p","?","c","#","-","p","p","p","#","?","=","=","e","p","p","p","p","p","p","-"]

    transpose = [[0 for x in range(width)] for y in range(heigth)]

    def composite_function(filename_complete_path, filename):

            global copy
            r = open(filename_complete_path, "r")
            i = 0
            while(i<13):
                change_string= r.readline()
                for c in change_string:
                        if(copy == 213): break
                        if(c == '\n'): continue
                        transpose[i][copy] = c
                        copy += 1
                change_string = ""
                copy = 0
                i+=1

            r.close()


            for i in range(0,13):
                for j in range(0,213):
                    pictures.append(symbols_array.index(transpose[i][j])+1)

            itr = 0
            for y in range (7,213,16):
                for x in range (16,3416,16):
                    if (x > 3408 or y > 213): break;
                    imgPath = path+str(pictures[itr])+".png"
                    img = Image.open(imgPath)
                    newLevel_image.paste(img,(x,y))
                    itr += 1
            print(itr)
            newLevel_image.save(newLevelPath+os.path.splitext(filename)[0]+'.png')


    # for z in range(1,50):
    composite_function("../Generated_levels/mario-1-1-edited_trans_new_"+str(z)+"_upRight.csv","mario-1-1-edited_trans_new_"+str(z)+".txt")


