#!/usr/bin/bash

director=`pwd`
list=`find $director -type f -name "*.txt"`
i=0
echo "Inceput\n"
for fisier in $list; do
    i=$(($i+1))
    echo "Fisierul $i - $fisier"
done
echo "Final\n"

echo "Alta runda"
media=0
nr=0
for fisier2 in $(ls | grep -E "*\.txt$"); do
    nr=$(($nr+1))
    linii=`wc -l $fisier2 | awk '{print $1}'`
    media=$(($media+$linii))
    echo "Fisierul: $fisier2 are $linii linii, suma = $media"
done

media=$(($media/$nr))

echo "Media = $media"
