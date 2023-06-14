#!/usr/bin/bash

linii=0
fisiere=0

for fisier in $(find); do
    if test -f $fisier && echo $fisier | grep -E -q "\.sh$"; then
        echo $fisier e fisier shell
        linii=$(($linii + $(grep -E -c -v -e "^$" -e "^#" -e "^ *$" $fisier)))
        echo si contine $linii de cod
        fisiere=$(($fisiere+1))
    fi
done

echo media este egala cu $(($linii/$fisiere))
