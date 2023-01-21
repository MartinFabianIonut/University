#!/usr/bin/bash

#Calculați numărul mediu de linii din fișierele text dintr-un director dat ca parametru

if test $# -lt 1; then
    echo Nu ati introdus niciun parametru
    exit 1
else
    if test ! -d $1; then
        echo Nu ati introdus un director valid
        exit 1
    else
        echo Bine
        linii=0
        fisieretext=0
        list=`find $1 -type f`
        for file in $list; do
            if file $file | grep -E -q "text"; then
                echo Fisierul $file este de tip text
                curent=`cat $file | wc -l`
                linii=$((linii+curent))
                echo Linii pana acum $linii
                fisieretext=$((fisieretext+1))
                echo -e Fisiere text pana acum $fisieretext \n
            fi
        done
    fi
fi
