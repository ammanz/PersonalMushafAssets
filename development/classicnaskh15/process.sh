#!/bin/bash

START=$1
END=$2

WIDTH=1152
HEIGHT=2048

for (( i=$START; i<=$END; i++ )) 
do
    INPUT="original/pg_$i.png"
    PNGOUTPUT="final/png/pg_$i.png"
    SVGOUTPUT="final/svg/pg_$i.svg"
    FINALOUTPUT="/home/ammanz/personal-mushaf/production/production-assets/classicnaskh15/images/classicnaskh15_pg_$i.png"
    echo -ne "$(($i-$START+1))/$(($END-$START+1))\r"
    convert $INPUT -deskew 80% -size ${WIDTH}x${HEIGHT} 2.png
    # python straight-lines.py 1.png
    convert 2.png -size ${WIDTH}x${HEIGHT} 3.ppm
    potrace --svg -k 0.85 -P ${WIDTH}x${HEIGHT} -o 4.svg 3.ppm
    inkscape -w $WIDTH -h $HEIGHT -b FFFFFF -y 1.0 4.svg --export-filename 5.png
    pngquant -f -o $FINALOUTPUT --quality=0-10 5.png
    mv 5.png $PNGOUTPUT
    mv 4.svg $SVGOUTPUT
done

rm 2.png 3.ppm
