#!/usr/bin/bash

for fisier in $*; do
    if test -r $fisier && test -x $fisier; then
        echo $fisier executabil si citibil
    fi
done
