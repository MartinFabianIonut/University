#!/usr/bin/bash

curent=`pwd`

list=`find $curent -perm -222 -type f`

for t in $list; do
    chmod u-w,g-w,o-w $t
done
