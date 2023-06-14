#!/usr/bin/bash



echo -n "Fisier:"; read fis



while true; do

    for d in "$@"; do

        if [ -e "$d/$fis" ]; then

            echo "Exista $fis in $d"
        else
            exit 1
        fi

    done

    sleep 1

done
