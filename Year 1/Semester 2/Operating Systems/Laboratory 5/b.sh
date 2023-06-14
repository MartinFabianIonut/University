#!/usr/bin/bash

for entitate in $*; do
    if test -f $entitate; then
        echo E fisier
    elif test -d $entitate; then
        echo E director
    elif echo $entitate | grep -E -q "^[0-9]+$"; then
        echo E numar
    else
        echo Nu stiu sunt prost
    fi
done
