def emaimare(a,b):
    #verifica daca numerele date sunt strict in ordine crescatoare, in ordinea a, b
    #input: a,b - nr intregi
    #output: rez - True, daca da, False, daca nu
    if a<b:
        return True
    return False


def edistinct(poz1,poz2,l):
    #verifica daca intr-o secventa sunt cel mult 3 numere distincte
    #input: poz1, poz2 - nr intregi, reprezentand pozitia de inceput si de sf a secv
    #       l - lista unde se gaseste secventa
    #output: rez - True, daca da, False, daca nu
    c=[l[poz1]] #creez o lista noua, unde adaug elementele distincte 
    lg=1
    i=poz1+1 #verific de la urmatorul
    ok=1
    while i<=poz2 and ok==1:
        k=0
        for x in c:
            if x==l[i]:
                k+=1
        if k==0:
            c.append(l[i])
            lg+=1
        if lg>3: #cand lungimea noii liste depaseste 3, deja pot iesi din while
            ok=0
        i+=1
    if ok==1:
        return True
    return False

def eegal(poz1,poz2,l):
    #verifica daca intr-o secventa sunt elemete egale
    #input: poz1, poz2 - nr intregi, reprezentand pozitia de inceput si de sf a secv
    #       l - lista unde se gaseste secventa
    #output: rez - True, daca da, False, daca nu
    primul = l[poz1]
    i = poz1+1 #verific de la urmatorul
    ok=1
    while i<=poz2 and ok==1:
        if l[i] != primul:
            ok=0
        i+=1
    if ok==1:
        return True
    return False


def testmaimare():
    #testeaza functia emaimare, pentru a verifica functionalitatea
    assert(emaimare(-1,0))
    assert(emaimare(1,0)==False)


def testdistincte():
    #testeaza functia edistinct, pentru a verifica functionalitatea
    assert(edistinct(0,5,[1,3,1,3,2,4])==False)
    assert(edistinct(0,5,[1,2,1,2,1,1]))
    assert(edistinct(0,5,[1,1,2,1,3,1,4,5,1,1,1]))
    assert(edistinct(2,4,[1,2,3,4,5,6]))

def testegale():
    #testeaza functia eegal, pentru a verifica functionalitatea
    assert(eegal(2,5,[1,2,3,3,3,3,6,2]))
    assert(eegal(3,5,[1,1,1,1,1,1,1]))
    assert(eegal(0,3,[1,2,3,3,4,2,3,2])==False)

def listamaimare(n,l):
    #se preteaza a se aduce specificatii suplimentare
    #se trateaza cazul in care, daca exista doua secvente de lungime maxima,
    #se afiseaza cea mai din dreapta sau stanga - conventie, aici, se alege dreapta
    #explicatie: conditia de lungime este stricta
    lista = []
    incep=0
    fin=1
    lungimemax=1
    sigur=0 #de aici va incepe secventa cautata
    while incep<n-1:
        while fin<n and emaimare(l[fin-1],l[fin])==True:
            fin+=1
        lungimecurenta = fin - incep
        if lungimecurenta>lungimemax:
            lungimemax=lungimecurenta
            sigur = incep
        incep+=1
        fin=incep+1
    fsigur=sigur+lungimemax-1 #aici se termina secventa cautata
    if fsigur>0: #aici verific daca exista secventa
        while sigur<=fsigur:
            lista.append(l[sigur])
            sigur+=1
    return lista


def listadistincte(n,l):
    #functia afla cea mai lunga secventa cu cel mult 3 el distincte
    #se preteaza a se aduce specificatii suplimentare
    #se trateaza cazul in care, daca exista doua secvente de lungime maxima,
    #se afiseaza cea mai din dreapta sau stanga - conventie, aici, se alege dreapta
    #explicatie: conditia de lungime este stricta
    #input: n - lungimea listei, l - lista
    #output: lista - acea secventa de lungime maxima, cu cel mult 3 elemente distincte
    lista = []
    celmulttrei=1
    incep=0
    fin=1
    lungimemax=1
    sigur=0 #de aici va incepe secventa cautata
    while incep<n-1:
        while fin<n and edistinct(incep, fin,l)==True:
            fin+=1
        lungimecurenta = fin - incep
        if lungimecurenta>lungimemax:
            lungimemax=lungimecurenta
            sigur = incep
        incep+=1
        fin=incep+1
    fsigur=sigur+lungimemax-1 #aici se termina secventa cautata
    while sigur<=fsigur:
        lista.append(l[sigur])
        sigur+=1
    return lista


def listaegale(n,l):
    #functia afla cea mai lunga secventa cu elemente egale
    #se preteaza a se aduce specificatii suplimentare
    #se trateaza cazul in care, daca exista doua secvente de lungime maxima,
    #se afiseaza cea mai din dreapta sau stanga - conventie, aici, se alege dreapta
    #explicatie: conditia de lungime este stricta
    #input: n - lungimea listei, l - lista
    #output: lista - acea secventa de lungime maxima, cu elemente egale
    lista = []
    incep=0
    fin=1
    lungimemax=1
    sigur=0 #de aici va incepe secventa cautata
    while incep<n-1:
        while fin<n and eegal(incep, fin,l)==True:
            fin+=1
        lungimecurenta = fin - incep
        if lungimecurenta>lungimemax:
            lungimemax=lungimecurenta
            sigur = incep
        incep+=1
        fin=incep+1
    fsigur=sigur+lungimemax-1 #aici se termina secventa cautata
    if fsigur>0: #aici verific daca exista secventa
        while sigur<=fsigur:
            lista.append(l[sigur])
            sigur+=1
    return lista


def smaimare(n,l):
    #functia va tipari lista de lungime maxima cu elemente in ordine strict crescatoare
    #daca nu exista o astfel de secventa, se tipareste mesaj
    s=""
    listafin = listamaimare(n,l)
    for el in listafin:
        s+=str(el)+" "
    if s=="":
        print("\nNu exista o secventa de lungime maxima, toate elementele sunt in ordine descrescatoare.\n")
    else:
        print("\nRezultat:",s,'\n')


def sdistincte(n,l):
    #functia va tipari lista de lungime maxima cu cel mult 3 elemente distincte
    #pentru ca lista are cel putin 3 elemente, intotdeauna va exista un rezultat
    s=""
    listafin = listadistincte(n,l)
    for el in listafin:
        s+=str(el)+" "
    print("\nRezultat:",s,'\n')

def segale(n,l):
    #functia va tipari lista de lungime maxima cu elemente egale
    #daca nu exista o astfel de secventa, se tipareste mesaj
    s=""
    listafin = listaegale(n,l)
    for el in listafin:
        s+=str(el)+" "
    if s=="":
        print("\nNu exista o secventa de lungime maxima, toate elementele aflate pe pozitii consecutive sunt distincte.\n")
    else:
        print("\nRezultat:",s,'\n')

def meniu():
    print("Aplicatia cu 2 proprietati, laborator 3")
    print("1. Citire lista de la tastatura:")
    print("2. Secventa de lungime maxima, cu numere strict crescatoare:")
    print("3. Secventa de lungime maxima, cu cel mult trei valori distincte:")
    print("4. Secventa de lungime maxima, cu elemente egale:")
    print("Pentru a parasi aplicatia, scrie exit.")
    print("Alege una dintre optiunile de mai sus pentru a incepe/continua!")


def citeste_lista(n,l):
    print("Introdu numarul de elemente ale listei, mai mare decat 2:")
    try:
        n=int(input(""))
        assert(n>2) #lungimea listei citite trebuie sa fie mai mare decat 2
    except:
        print("\nNumarul de elemente trebuie sa fie o valoare intreaga, mai mare sau egala cu 3!\n")
        return
    print("Introdu elementele:")
    i=1
    while i<=n:
        try:
            el = int(input(""))
            l.append(el)
        except:
            print("\nNu este buna valoarea. Trebuia introdusa o valoare intreaga!\n")
            return
        i+=1
    return n

def run():
    l = []
    n=0
    while True:
        meniu()
        cmd = input(">>>")
        if cmd == "exit":
            return
        if cmd == "1":
            l = [] #de fiecare data se reinitializeaza lista cu nimic,
            #pt ca altfel s-ar tot adauga cu l.append din citeste_lista
            n=citeste_lista(n,l)
        if cmd == "2":
            try:
                assert(len(l)!=0) #daca nu sunt elemente, nu se poate continua
                smaimare(n,l)
            except:
                print("\nNu ati introdus elementele listei! Incepeti cu pasul 1!\n")
        if cmd == "3":
            try:
                assert(len(l)!=0) #daca nu sunt elemente, nu se poate continua
                sdistincte(n,l)
            except:
                print("\nNu ati introdus elementele listei! Incepeti cu pasul 1!\n")
        if cmd == "4":
            try:
                assert(len(l)!=0) #daca nu sunt elemente, nu se poate continua
                segale(n,l)
            except:
                print("\nNu ati introdus elementele listei! Incepeti cu pasul 1!\n")
        if cmd != "1" and cmd != "2" and cmd != "3" and cmd != "4":
            print("\nComanda este invalida!\n")


def main():
    testmaimare()
    testdistincte()
    testegale()
    run()

main()
