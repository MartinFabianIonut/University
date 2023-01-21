#!/usr/bin/bash

#Afișați primele 5 linii și ultimele 5 linii din fișierele de tip text din directorul curent; dacă un fișier are mai puțin de 10 linii, afișați fișierul complet (comenzi: head, tail, find, file, wc).

list=`find ${pwd} -type f`

for file in $list; do
    if file $file | grep -E -q "text"; then
        lg=`cat $file | wc -l`
        if test $lg -lt 10; then
            echo Fisierul $file va fi afisat complet
            af=`cat $file`
            echo -e "$af\n"
        else
            primele=`cat $file | head -5`
            ultimele=`cat $file | tail -5`
            echo Fisierul $file - de aici afisez pe rand
            echo Primele 5 linii sunt:
            echo -e "$primele\n"
            echo Ultimele 5 linii sunt:
            echo -e "$ultimele\n"
        fi
    fi
done
