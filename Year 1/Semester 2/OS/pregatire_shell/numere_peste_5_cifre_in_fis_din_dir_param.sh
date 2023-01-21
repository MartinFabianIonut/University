#!/usr/bin/bash

#Afișați numele fișierelor dintr-un director dat ca parametru care conțin numere de peste 5 cifre.
if test $# -lt 1; then
    echo Nu ati introdus niciun parametru
    exit 1
else
    if test ! -d $1; then
        echo Nu e director
        exit 1
    else 
        list=`find $1 -type f`
        for file in $list; do
            if grep -E -q "^.*[0-9]{5,}.*$" $file; then
                echo Fisierul $file contine numere de peste 5 cifre
            fi
        done
    fi
fi

