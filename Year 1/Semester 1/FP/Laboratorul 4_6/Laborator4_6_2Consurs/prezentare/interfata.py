from business.servicii import iduri_participanti,functionalitate6,functionalitate1a,tipareste_participanti,functionalitate4a,functionalitate4b,functionalitate4c,functionalitate5a, functionalitate5b, functionalitate3a, functionalitate3b, functionalitate3c, functionalitate2a, functionalitate2b, functionalitate2c
from exceptii.erori import RepoError, ValidError


def meniu():
    # afiseaza optiunile aplicatiei - adica functionalitatile
    print("")
    print("Aplicatia pentru laboratoarele 4-6")
    print("1. Adauga un scor la un participant")
    print("2. Modificare scor")
    print("3. Tipareste lista de participanti")
    print("4. Operatii pe un subset de participanti")
    print("5. Filtrare")
    print("6. Undo (reface ultima operatie, adica ori cea de f1, ori cea de f2)")
    print("7. Afiseaza toti participantii (de verificare)")
    print("Pentru a parasi aplicatia, scrie exit.")
    print("Alege una dintre optiunile de mai sus pentru a incepe/continua! (se va scrie doar cifra, de la 1 la 7)")

def submeniu(i):
    #pentru fiecare dintre functionalitatile aplicatiei, se afiseaza subfunctionalitatile aferente
    if i==1:
        print("a. Creeaza un nou concurent")
        #print("b. Adauga scorurile pentru cele 10 probe")
    if i==2:
        print("a. Sterge scorul pentru un participant dat")
        print("b. Sterge scorul pentru un interval de participanti")
        print("c. Inlocuieste scorul unui participant")
    if i==3:
        print("a. Tipareste participantii care au un scor mai mic decat unul dat")
        print("b. Tipareste participantii ordonat crescator")
        print("c. Tipareste participantii care au un scor mai mare decat unul dat, ordonati dupa scor")
    if i==4:
        print("a. Calculeaza media scorurilor pentru un interval dat")
        print("b. Calculeaza scorul minim pentru un interval de participanti dat")
        print("c. Tipareste participantii dintr-un interval dat care au scorul multiplu de 10")
    if i==5:
        print("a. Filtrare participanti care au scorul multiplu unui numar dat.")
        print("b. Filtrare participanti care au scorul mai mic decat un scor dat")

def run():
    stack = []
    lista_toti=[]
    while True:
        meniu()
        cmd_init = ""
        try:
            cmd_init = input(">>>")
            cmd = int(cmd_init)
        except ValueError:
            if cmd_init == "exit":
                return
            else:
                print("Ce ati introdus nu corespunde niciuneia dintre comenzile aplicatiei! Introduceti o valoare numerica intreaga, cuprinsa intre 1 si 7 sau exit, daca doriti sa parasiti aplicatia.\n")
                continue
        submeniu(cmd)
        if cmd == 1:
            sub = input(">>>")
            if sub == "a":
                try:
                    id_participant = int(input("Introduceti id-ul noului participant (numar natural nenul):"))
                    scor = []
                    for i in range(10):
                        print("Scor proba ",i+1," :")
                        scor.append(float(input("")))
                    functionalitate1a(lista_toti, id_participant, scor)
                    stack.append(["sterge_participantul_adaugat",id_participant])
                except ValidError as ve:
                    print(ve)
                except RepoError as re:
                    print(re)
                except:
                    print("Ati introdus un caracter sau un sir de caractere imposibil de convertit la numar intreg sau real! Reincercati!\n")
            else:
                print("Ce ati introdus nu corespunde niciuneia dintre comenzile aplicatiei! Introduceti o litera (aici doar litera a) sau exit, daca doriti sa parasiti aplicatia.\n")
        elif cmd == 2:
            sub = input(">>>")
            if sub == "a":
                print("\nAlegeti pentru care (unul) dintre urmatorii participanti (id) doriti sa stergeti scorul:\n")
                iduri = iduri_participanti(lista_toti)
                print(iduri+"\n")
                try:
                    care = int(input(">>>"))
                    ce_scor_are_care = functionalitate2a(lista_toti, care)
                    stack.append(["refa_scorul_de_dinainte",care, ce_scor_are_care])
                except ValidError as ve:
                    print(ve)
                except:
                    print("\nValoare invalida pentru id!\n")
            elif sub == "b":
                print("Pentru a introduce intervalul, dati, pe rand, doua valori numere naturale, a si b, cu a < b, a >= 1:")
                try:
                    poz1 = int(input("Introduceti unde incepe intervalul:"))
                    poz2 = int(input("Introduceti unde se termina intervalul:"))
                    id_de_la_care = []
                    scoruri_sterse = []
                    if functionalitate2b(lista_toti, poz1, poz2, id_de_la_care,scoruri_sterse)==-1:
                        print("\nNu ati introdus valori corecte pentru un interval, adica a < b si b <= numarul total de participanti.\n")
                    else:
                        functionalitate2b(lista_toti, poz1, poz2, id_de_la_care,scoruri_sterse)
                        stack.append(["sterge_scoruri_din_interval",poz1,poz2,id_de_la_care,scoruri_sterse]) #3 = cod pentru stergere scor interval participanti, retine poz1,poz2, id-urile part din interval si scorurile initiale
                except ValueError:
                    print("valoare invalida!\n")
            elif sub == "c":
                print("\nAlegeti pentru care (unul) dintre urmatorii participanti (id) doriti sa modificati scorul:\n")
                iduri = iduri_participanti(lista_toti)
                print(iduri + "\n")
                try:
                    care = int(input(">>>"))
                    scor = []
                    for i in range(10):
                        print("Scor proba ",i+1," :")
                        scor.append(float(input("")))
                    scor_init = 0
                    scor_initial=functionalitate2c(lista_toti, care, scor,scor_init)
                    stack.append(["modifica_scorul",care,scor_initial])
                except ValidError as ve:
                    print(ve)
                except:
                    print("\nValoare invalida!\n")
            else:
                print("Ce ati introdus nu corespunde niciuneia dintre comenzile aplicatiei! Introduceti o litera (aici a,b sau c) sau exit, daca doriti sa parasiti aplicatia.\n")
        elif cmd == 3:
            sub = input(">>>")
            if sub == "a":
                try:
                    scor_dat = float(input("Dati un scor, pentru a afisa participantii cu scor mai mic decat respectivul (10 <= scor <= 100): "))
                    assert(scor_dat>=10 and scor_dat<=100)
                    if functionalitate3a(lista_toti, scor_dat)==[]:
                        print("\nNu exista participanti cu un scor mai mic decat cel introdus!\n")
                    else:
                        print("\nParticipantii cu scor mai mic decat " + str(scor_dat) + " sunt: \n")
                        de_af = functionalitate3a(lista_toti, scor_dat)
                        for _p in de_af:
                            print("Participantul " + str(_p["id_participant"]) + ", cu scorul: " + str(_p["scor"]))
                except:
                    print("\nScorul introdus nu este corespunzator!\n")
            elif sub == "b":
                print("\nParticipantii ordonati crescator dupa scor sunt: \n")
                de_af = functionalitate3b(lista_toti)
                for _p in de_af:
                    print("Participantul " + str(_p["id_participant"]) + ", cu scorul: " + str(_p["scor"]))
            elif sub == "c":
                try:
                    scor_dat = float(input("Dati un scor, pentru a afisa participantii cu scor mai mare decat respectivul, ordonati crescator (10 <= scor <= 100): "))
                    assert(scor_dat>=10 and scor_dat<=100)
                    if functionalitate3c(lista_toti, scor_dat)==[]:
                        print("\nNu exista participanti cu un scor mai mare decat cel introdus!\n")
                    else:
                        print("\nParticipantii cu scor mai mare decat " + str(scor_dat) + " si ordonati crescator sunt: \n")
                        de_af = functionalitate3c(lista_toti,scor_dat)
                        for _p in de_af:
                            print("Participantul " + str(_p["id_participant"]) + ", cu scorul: " + str(_p["scor"]))
                except:
                    print("\nScorul introdus nu este corespunzator!\n")
            else:
                print("Ce ati introdus nu corespunde niciuneia dintre comenzile aplicatiei! Introduceti o litera (aici a,b sau c) sau exit, daca doriti sa parasiti aplicatia.\n")
        elif cmd == 4:
            sub = input(">>>")
            if sub == "a":
                print("Pentru a introduce intervalul, dati, pe rand, doua valori numere naturale, a si b, cu a < b, a >= 1:")
                try:
                    poz1 = int(input("Introduceti unde incepe intervalul:"))
                    poz2 = int(input("Introduceti unde se termina intervalul:"))
                    if functionalitate4a(lista_toti, poz1, poz2)!=-1:
                        print("\nMedia pentru intervalul dat este: " + str(functionalitate4a(lista_toti,poz1,poz2)) + "\n")
                    else:
                        print("\nNu ati introdus valori corecte pentru un interval, adica a < b si b <= numarul total de participanti.\n")
                except ValueError:
                    print("Valoare invalida!\n")
            elif sub=="b":
                print("Pentru a introduce intervalul, dati, pe rand, doua valori numere naturale, a si b, cu a < b, a >= 1:")
                try:
                    poz1 = int(input("Introduceti unde incepe intervalul:"))
                    poz2 = int(input("Introduceti unde se termina intervalul:"))
                    if functionalitate4b(lista_toti, poz1, poz2)==-1:
                        print("\nNu ati introdus valori corecte pentru un interval, adica a < b si b <= numarul total de participanti.\n")
                    else:
                        print("\nLista participantilor care au scorul minim din intervalul dat: \n")
                        l_scor_min = functionalitate4b(lista_toti, poz1, poz2)
                        for _participant in l_scor_min:
                            print("Participantul cu id-ul: "+str(_participant["id_participant"]) + " si cu scorul minim, de " + str(_participant["scor"]))
                except ValueError:
                    print("Valoare invalida!\n")
            elif sub=="c":
                print("Pentru a introduce intervalul, dati, pe rand, doua valori numere naturale, a si b, cu a < b, a >= 1:")
                try:
                    poz1 = int(input("Introduceti unde incepe intervalul:"))
                    poz2 = int(input("Introduceti unde se termina intervalul:"))
                    if functionalitate4c(lista_toti, poz1, poz2)==[]:
                        print("\nNu exista concurenti cu scor multiplu de 10.\n")
                    elif functionalitate4c(lista_toti, poz1, poz2)==-1:
                        print("\nNu ati introdus valori corecte pentru un interval, adica a < b si b <= numarul total de participanti.\n")
                    else:
                        print("\nParticipantii care au un scor multiplu de zece sunt: \n")
                        multiplu = functionalitate4c(lista_toti, poz1, poz2)
                        for _participant in multiplu:
                            print("Participantul cu id-ul: "+str(_participant["id_participant"]) + " si cu scorul " + str(_participant["scor"]))
                except ValueError:
                    print("Valoare invalida!\n")
            else:
                print("Ce ati introdus nu corespunde niciuneia dintre comenzile aplicatiei! Introduceti o litera (aici a sau c) sau exit, daca doriti sa parasiti aplicatia.\n")
        elif cmd == 5:
            sub = input(">>>")
            if sub == "a":
                try:
                    scor_dat = float(input("Dati un numar > 0 (de asemenea <=100), pentru a filtra participantii care nu au scorul multiplu al acelui numar: "))
                    assert(scor_dat>0 and scor_dat<=100)
                    lista_filtrata = functionalitate5a(lista_toti, scor_dat)
                    if lista_filtrata==-1:
                        print("\nNu sunt asemenea cazuri de filtrare!\n")
                    else:
                        o = 0
                        print("\nFiltrarea participantilor care nu au scorul multiplu de "+str(scor_dat)+":")
                        for _participant in lista_toti:
                            o += 1
                            oki = 0
                            for ordi in lista_filtrata:
                                if o == ordi:
                                    oki = 1
                                    print("Participantul " + str(_participant["id_participant"]) + " <-> 0.0.")
                            if oki == 0:
                                print("Participantul " + str(_participant["id_participant"]) + " <-> " + str(_participant["scor"]) + ".")
                except:
                    print("\nScorul introdus nu este corespunzator!\n")
            elif sub == "b":
                try:
                    scor_dat = float(input("Dati un scor, pentru a filtra participantii cu scor mai mic decat respectivul (10 <= scor <= 100): "))
                    assert(scor_dat>=10 and scor_dat<=100)
                    lista_filtrata = functionalitate5b(lista_toti, scor_dat)
                    if lista_filtrata==-1:
                        print("\nNu sunt asemenea cazuri de filtrare!\n")
                    else:
                        o = 0
                        print("\nFiltrarea participantilor cu scor mai mic decat "+str(scor_dat)+":")
                        for _participant in lista_toti:
                            o += 1
                            oki = 0
                            for ordi in lista_filtrata:
                                if o == ordi:
                                    oki = 1
                                    print("Participantul " + str(_participant["id_participant"]) + " <-> 0.")
                            if oki == 0:
                                print("Participantul " + str(_participant["id_participant"]) + " <-> " + str(_participant["scor"]) + ".")
                except:
                    print("\nScorul introdus nu este corespunzator!\n")
            else:
                print("Ce ati introdus nu corespunde niciuneia dintre comenzile aplicatiei! Introduceti o litera (aici a sau c) sau exit, daca doriti sa parasiti aplicatia.\n")
        elif cmd==6:
            if stack != []: #verifica daca se mai poate face undo
                functionalitate6(lista_toti, stack)
                tipareste_participanti(lista_toti)
            else:
                print("\nNu mai sunt operatii pentru care sa se faca undo!\n")
        elif cmd==7:
            tipareste_participanti(lista_toti)
        else:
            print("Ce ati introdus nu corespunde niciuneia dintre comenzile aplicatiei! Introduceti o valoare numerica intreaga, cuprinsa intre 1 si 7 sau exit, daca doriti sa parasiti aplicatia.\n")


