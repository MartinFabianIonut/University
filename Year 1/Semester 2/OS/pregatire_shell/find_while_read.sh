#!/usr/bin/bash

find $1 -type f | while read F; do
    echo $F
done
