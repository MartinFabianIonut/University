#!/usr/bin/bash
sum=0
poz=0
for el in $*; do
    if test $(($poz%3)) -eq 0 && test $(($el%2)) -eq 0; then
        sum=$(($sum+$el))
        echo $sum
    fi
    poz=$(($poz+1))
done
