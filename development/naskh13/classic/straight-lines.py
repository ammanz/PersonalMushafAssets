"""
@file hough_lines.py
@brief This program demonstrates line finding with the Hough transform
"""
import sys
import math
import cv2 as cv
import numpy as np

def sortArea(val):
    return cv.contourArea(val)

def getLargestRect(img):
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    
    ret,thresh = cv.threshold(gray,127,255,1)
        
    contours,h = cv.findContours(thresh,1,2)

    contours.sort(key = sortArea, reverse = True)
    
    return cv.minAreaRect(contours[0])

def getSideBoundaries(rect):
    cx = rect[0][0]
    a = rect[1][0]
    boundaries = [int(cx - a/2) + 20, int(cx + a/2) - 20]
    return boundaries


def main(argv):
    
    filename = argv[0]
    # Loads an image
    src = cv.imread(cv.samples.findFile(filename), cv.IMREAD_UNCHANGED)
    # Check if image is loaded fine
    if src is None:
        print ('Error opening image!')
        print ('Usage: hough_lines.py [image_name -- default ' + default_file + '] \n')
        return -1
    
    
    dst = cv.Canny(src, 50, 200, None, 3)
    
    # Copy edges to the images that will display the results in BGR
    cdstP = np.copy(src)
    
    linesP = cv.HoughLinesP(dst, 1, np.pi / 180, 50, None, 600, 1)
    
    largestRect = getLargestRect(src)
    
    print(largestRect) 
    
    sideBoundaries = getSideBoundaries(largestRect)
    
    if linesP is not None:
        for i in range(0, len(linesP)):
            l = linesP[i][0]
            cv.line(cdstP, (sideBoundaries[0], l[1]), (sideBoundaries[1], l[3]), (255,255,255), 2, cv.LINE_AA)
    
    cv.imwrite("2.png", cdstP)
    
    return 0
    
if __name__ == "__main__":
    main(sys.argv[1:])
