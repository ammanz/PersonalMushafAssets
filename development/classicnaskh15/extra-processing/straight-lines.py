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
    
    
    dst = cv.Canny(src, 50, 200, None, 3)
    
    # Copy edges to the images that will display the results in BGR
    cdstP = np.copy(src)
    
    linesP = cv.HoughLinesP(dst, 1, np.pi / 180, 50, None, 200, 1)
    
    if linesP is not None:
        for i in range(0, len(linesP)):
            l = linesP[i][0]
            cv.line(cdstP, (0, l[1]), (1152, l[3]), (255,255,255,255), 5, cv.LINE_AA)
    
    cv.imwrite("2.png", cdstP)
    
    return 0
    
if __name__ == "__main__":
    main(sys.argv[1:])
