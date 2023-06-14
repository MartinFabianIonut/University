from datetime import datetime

def main():
    zi_cmd = input("Dati, pe rand, ziua, luna si anul nasterii:\nZiua = >>>")
    luna_cmd = input("Luna = >>>")
    an_cmd = input("Anul = >>>")
    varsta = 0
    try:
        zi = int(zi_cmd)
        assert(zi>0 and zi<32)
    except:
        print("Ziua nu este un numar valid!")
        return
    try:
        luna = int(luna_cmd)
        assert(luna>0 and luna<13)
    except:
        print("Luna nu este un numar valid!")
        return
    try:
        an = int(an_cmd)
        assert(an>0 and an<2022)
    except:
        print("Anul nu este un numar valid!")
        return
    zic = datetime.now().day
    lunac = datetime.now().month
    anc = datetime.now().year
    obisnuit = [0,31,28,31,30,31,30,31,31,30,31,30,31]
    bisect = [0,31,29,31,30,31,30,31,31,30,31,30,31]
    if an != anc:
        if an%4 == 0 and an%100 != 0 or an%400 == 0:
            varsta = bisect[luna] - zi
            lunaplus = luna+1
            while lunaplus <= 12:
                varsta += bisect[lunaplus]
                lunaplus += 1
        else:
            varsta = obisnuit[luna] - zi
            lunaplus = luna + 1
            while lunaplus <= 12:
                varsta += obisnuit[lunaplus]
                lunaplus += 1
        i = an+1
        while i < anc:
            luna = 1
            if i%4 == 0 and i%100 != 0 or i%400 == 0:
                varsta += bisect[luna]
                lunaplus = luna + 1
                while lunaplus <= 12:
                    varsta += bisect[lunaplus]
                    lunaplus += 1
            else:
                varsta += obisnuit[luna]
                lunaplus = luna + 1
                while lunaplus <= 12:
                    varsta += obisnuit[lunaplus]
                    lunaplus += 1
            i += 1
        luna = 1
        if i%4 == 0 and i%100 != 0 or i%400 == 0:
            
            lunaplus = 1
            while lunaplus < lunac:
                varsta += bisect[lunaplus]
                lunaplus += 1
            varsta += zic
        else:
            
            lunaplus = 1
            while lunaplus < lunac:
                varsta += obisnuit[lunaplus]
                lunaplus += 1
            varsta += zic 
    else:
        if an%4 == 0 and an%100 != 0 or an%400 == 0:
            varsta = bisect[luna] - zi
            lunaplus = luna+1
            while lunaplus <= lunac:
                varsta += bisect[lunaplus]
                lunaplus += 1
            varsta += zic - bisect[lunac]
        else:
            varsta = obisnuit[luna] - zi
            lunaplus = luna + 1
            while lunaplus <= lunac:
                varsta += obisnuit[lunaplus]
                lunaplus += 1
            varsta += zic - obisnuit[lunac]
    print (varsta)
    input()

main()
