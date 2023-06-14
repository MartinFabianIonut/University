#!/usr/bin/bash

if test $# -lt 1; then
    echo Nu
    exit 1
elif test ! -d $1; then
    echo NU2
    exit 1
else
    for fis in $(ls $1 | grep -E "\.c$"); do
        echo Nume fis c: $fis
        rm $1/$fis
    done
    sorted=""
    for fis2 in $(ls $1); do
        echo $fis2
    done | sort -n
fi
