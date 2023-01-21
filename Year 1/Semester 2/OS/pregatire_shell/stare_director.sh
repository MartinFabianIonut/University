#!/usr/bin/bash

if test $# -lt 1; then
    echo Nu ai introdus arg
    exit 1
elif test ! -d $1; then
    echo Nu e director
    exit 1
else
    list=`find $1`
    init=0
    for ent in $list; do
        ad=`du -b $ent | awk '{print $1}'`
        init=$((init+ad))
    done
    echo Initial avem $init bytes in directorul dat
    while true; do
        dupa=0
        for ent2 in $list; do
            ad2=`du -b $ent2 | awk '{print $1}'`
            dupa=$((dupa+ad2))
        done
        echo Momentan avem $dupa bytes
        if test $init -ne $dupa; then
            echo S-a modificat starea initiala a directorului
            exit 0
        fi
        sleep 1
    done
fi
