#!/bin/bash

for i in {1..848}; do
    INPUT="13_pg_$i.png"
    OUTPUT="deskewed/13_pg_$i.png"
    deskew -o $OUTPUT -b FFFFFF  -f b1 $INPUT
done
