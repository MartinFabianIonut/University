#!/usr/bin/bash

for i in $@; do
    if test $(($i%15)) -eq 0; then
        echo $i
    fi
done
