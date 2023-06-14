#!/usr/bin/bash

F=$1
N=0
while [ $N -lt 200 ]; do
    K=`cat $F`
    echo "K = $K \n"
    K=`expr $K + 1`

    echo $K > $F
    N=`expr $N + 1`
done
