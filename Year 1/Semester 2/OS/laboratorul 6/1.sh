#!/usr/bin/bash                                                                                                                                                          
suma=0
loc=0

list=`find $1 -maxdepth 1 -type f`
for entitate in $list; do
    if test -f $entitate; then
        loc=`du -b $entitate | awk '{print $1}'`
        suma=$((suma+loc))
        echo Local avem $loc si suma este $suma        
    fi
done
