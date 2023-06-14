#!/usr/bin/bash

if test $# -lt 1; then
    echo insuficient
    exit 1
fi
all=""
for file in $@; do
    bibl=""
    if file $file | grep -E -q "C source"; then
        bibl=$(grep -E "^#include" $file)
        all+=$bibl"\n"
    else
        echo NU
    fi

done

echo -e "$all" | sort | uniq > stocare
