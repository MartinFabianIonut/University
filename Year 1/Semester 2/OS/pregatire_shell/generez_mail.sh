#!/usr/bin/bash

if [ -z "$1" ]; then
    echo "Please provide one input file"
    exit 1
fi

if [ ! -f "$1" ]; then
    echo "The given argument is not a file"
    exit 1
fi

result=""
for u in $(cat "$1"); do
    result="$u@scs.ubbcluj.ro,$result"
done

result=$(echo $result | sed -E "s/,$//")

echo $result
