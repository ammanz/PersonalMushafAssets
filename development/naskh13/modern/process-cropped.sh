#!/bin/bash

# 29 57 85 113 141 169 197 225 253 281 309 337 365 421 505 533 559 587 613 641 667 727
# pg 2: 1211x1777
# pg 3: 1209x1776
# rest: 1199x1806

START=$1
END=$2

WIDTH=1199
HEIGHT=1806
 
for (( i=$START; i<=$END; i++ )) 
do
    INPUT="cropped/original/pg_$i.png"
    SVGOUTPUT="cropped/final/svg/pg_$i.svg"
    PNGOUTPUT="cropped/final/png/pg_$i.png"
    echo -ne "$(($i-$START+1))/$(($END-$START+1))\r"
    convert 0.png -size ${WIDTH}x${HEIGHT} 1.ppm
    potrace --svg -k 0.5 -t 3 -P ${WIDTH}x${HEIGHT} -o 2.svg 1.ppm
    inkscape -w $WIDTH -h $HEIGHT -b FFFFFF -y 1.0 2.svg --export-filename 3.png
    python straight-lines.py 3.png
    convert 4.png -size ${WIDTH}x${HEIGHT} 5.ppm
    potrace --svg -k 0.7 -P ${WIDTH}x${HEIGHT} -o final.svg 5.ppm
    mv 4.png $PNGOUTPUT
    mv final.svg $SVGOUTPUT
done

rm 0.png 1.ppm 2.svg 3.png 5.ppm
