#!/bin/bash

START=$1
END=$2

for (( i=$START; i<=$END; i++ )) 
do
    INPUT="original/13_pg_$i.png"
    OUTPUT="deskewed-trimmed/pg_$i.png"
    echo -ne "$(($i-$START+1))/$(($END-$START+1))\r"
    convert $INPUT -deskew 80% -size 1300x1900 1.png
    convert 1.png -size 1300x1900 2.ppm
    potrace --svg -k 0.65 -P 1300x1900 -o 3.svg 2.ppm
    inkscape -w 1300 -h 1900 -b FFFFFF -y 1.0 3.svg --export-filename 4.png
    convert -trim 4.png -size 1300x1900 $OUTPUT
done

rm 1.png 2.ppm 3.svg 4.png
