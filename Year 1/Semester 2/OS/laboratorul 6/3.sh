#!/usr/bin/bash                                                                                                                                                          
users="-e"
if [ $# -gt 0 ]; then
    users=""
    for user in $@; do
        users="$users -u $user"
    done
fi

while true; do
#    clear
    ps -f $users | awk 'NR > 1{print $1}' | sort | uniq -c | sort -n -r -k1
    sleep 1
done
