import sys
import math
import cv2 as cv
import numpy as np
def main(argv):
    
    filename = argv[0]
    # Loads an image
    src = cv.imread(cv.samples.findFile(filename), cv.IMREAD_UNCHANGED)
    # Check if image is loaded fine
    if src is None:
        print ('Error opening image!')
        print ('Usage: hough_lines.py [image_name -- default ' + default_file + '] \n')
        return -1
    
    
    borders = [((142, 122), (1435 , 151)), ((142, 122), (174, 1982)), 
               ((1407, 122), (1435, 1982)), ((142, 1952), (1435, 1982))]
    
    for i in range(0, len(borders)):
        cv.rectangle(src, borders[i][0], borders[i][1], (255,255,255,255), -1)
    
    cv.imwrite("0.png", src)
    
    return 0
    
if __name__ == "__main__":
    main(sys.argv[1:])
