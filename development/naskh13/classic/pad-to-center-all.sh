#!/bin/bash

START=$1
END=$2

for (( i=$START; i<=$END; i++ )) 
do
    INPUT="deskewed-trimmed/pg_$i.png"
    PNGOUTPUT="final/png/pg_$i.png"
    SVGOUTPUT="final/svg/pg_$i.svg"
    echo -ne "$(($i-$START+1))/$(($END-$START+1))\r"
    python center.py $INPUT
    convert 0.png -size 1400x2000 1.ppm
    potrace --svg --flat -k 0.9 -P 1400x2000 -o 2.svg 1.ppm
    inkscape -w 1400 -h 2000 -b FFFFFF -y 1.0 2.svg --export-filename 3.png
    mv 3.png $PNGOUTPUT
    mv 2.svg $SVGOUTPUT
done

rm 0.png 1.ppm
