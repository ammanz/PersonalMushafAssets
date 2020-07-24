#!/bin/bash

# 29 57 85 113 141 169 197 225 253 281 309 337 365 421 505 533 559 587 613 641 667 727
# pg 2: 1211x1777
# pg 3: 1209x1776
# rest: 1199x1806

START=$1
END=$2

WIDTH=1600
HEIGHT=2100
 
for (( i=$START; i<=$END; i++ )) 
do
    INPUT="uncropped/original/pg_$i.png"
    SVGOUTPUT="uncropped/final/svg/pg_$i.svg"
    PNGOUTPUT="uncropped/final/png/pg_$i.png"
    echo -ne "$(($i-$START+1))/$(($END-$START+1))\r"
    python remove-borders.py $INPUT
    convert 0.png -size ${WIDTH}x${HEIGHT} 0.ppm
    potrace --svg -k 0.59 -t 3 -P ${WIDTH}x${HEIGHT} -o 1.svg 0.ppm
    inkscape -w $WIDTH -h $HEIGHT -b FFFFFF -y 1.0 1.svg --export-filename 2.png
    python straight-lines.py 2.png
    convert 3.png -size ${WIDTH}x${HEIGHT} 4.ppm
    potrace --svg -k 0.9 -P ${WIDTH}x${HEIGHT} -o final.svg 4.ppm
    mv 3.png $PNGOUTPUT
    mv final.svg $SVGOUTPUT
done

rm 0.png 0.ppm 1.svg 2.png 4.ppm
