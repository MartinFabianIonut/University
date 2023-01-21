#!/usr/bin/bash

echo 0 > a.txt

./inc.sh a.txt &
./inc.sh a.txt &
./inc.sh a.txt &
