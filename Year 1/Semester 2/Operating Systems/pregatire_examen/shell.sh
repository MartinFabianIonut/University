linii=`grep -E " *[A-Z]" fisiercuvinte.txt`
echo $linii
for linie in $(awk '{print $0}' fisiercuvinte.txt); do
    l=`$linie | grep -E " *[A-Z]"`
    echo da $l
    if test -n $l; then
        echo este $l
    fi
done
