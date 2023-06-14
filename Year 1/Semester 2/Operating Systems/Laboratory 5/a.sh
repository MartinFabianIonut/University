#!/usr/bin/bash

director=$1
cate=0
list=`find $director -type f -name "*.c"`
for F in $list; do
    cate=$(($cate+1))
    echo $F
done
echo "Am" $cate "fisiere .c"

