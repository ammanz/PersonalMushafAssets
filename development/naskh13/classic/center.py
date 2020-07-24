from PIL import Image
import numpy as np
import cv2
import sys

def sortArea(val):
    return cv2.contourArea(val)

def getPadding(cx, cy, a, b, x, y):
    tL = [int(cx - a/2), int(cy - b/2)]
    bR = [int(cx + a/2), int(cy + b/2)]
    TB = int((2000 - b)/2)
    LR = int((1400 - a)/2)
    
    ST = TB - tL[1]
    SB = TB - (y - bR[1])
    SL = LR - tL[0]
    SR = LR - (x - bR[0])
    
    return [ST, SB, SL, SR]

def getLargestRect(img):
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    
    ret,thresh = cv2.threshold(gray,127,255,1)
        
    contours,h = cv2.findContours(thresh,1,2)

    contours.sort(key = sortArea, reverse = True)
    
    return contours[0]

image = sys.argv[1]
if len(sys.argv) <= 2:
    offset = 0
else:
    offset = int(sys.argv[2])
    
img = cv2.imread(image)
output = cv2.imread(cv2.samples.findFile(image), cv2.IMREAD_UNCHANGED)
height, width = img.shape[:2]

largestRect = getLargestRect(img)

rect = cv2.minAreaRect(largestRect)

paddings = getPadding(rect[0][0] + int(offset/2), rect[0][1], rect[1][0] - offset, rect[1][1], width, height)

final = cv2.copyMakeBorder(output, paddings[0], paddings[1], paddings[2], paddings[3], cv2.BORDER_CONSTANT, value=[255, 255, 255])

cv2.imwrite('0.png', final)








#def crop(image_path, coords, saved_location):
#    image_obj = Image.open(image_path)
#    cropped_image = image_obj.crop(coords)
#    cropped_image.save(saved_location)
#x,y,w,h = cv2.boundingRect(contours[0])
#
#
#cropped_image_path =  'cropped/'  + image[9:len(image)-4] + '-cropped.png'
#
#print(cropped_image_path)
#
#crop(image, (x, y, x+w,y+h ), cropped_image_path)


