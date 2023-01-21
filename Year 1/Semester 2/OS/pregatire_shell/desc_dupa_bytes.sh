#!/usr/bin/bash

if test $# -lt 1; then
    echo Nu ati introdus niciun argument
else
    for fisier in $@ ;do
        if [ ! -f $fisier ]; then
            echo $fisier nu e fisier
        else
            du -b $fisier
        fi

    done | sort -n -r
fi
