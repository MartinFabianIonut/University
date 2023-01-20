from domain.participant import creeaza_participant, este_interval, get_scor
from validation.participant_valid import valideaza_participant, valideaza_scor,exista_participant
from infrastructura.participanti_repo import adauga_participant_lista
from exceptii.erori import RepoError

def srv_adauga_la_lista(lista_toti, id_participant, scor):
    #functie care creeaza un participant pe baza id si scor, valideaza aceast participant si daca e valid il adauga in lista lista_toti de
    #participanti unic identificabili, doar daca nu are egal
    #input: lista_toti - lista de participanti unic identificabili prin id_participant
    #       id_participant - int > 0
    #       scor - float >= 0
    #output: -, daca totul se desfasoara cu succes
    #raises: exceptii preluate de la funciile apelate
    participant = creeaza_participant(id_participant, scor)
    valideaza_participant(participant)
    adauga_participant_lista(lista_toti, participant)

def functionalitate1a(lista_toti,id_participant,scor):
    valideaza_scor(scor)
    scor_total = 0
    for i in range(len(scor)):
        scor_total += scor[i]
    #valideaza_scor(scor_total)
    srv_adauga_la_lista(lista_toti,id_participant,scor_total)
    
def iduri_participanti(lista_toti):
    iduri = ""
    for _participant in lista_toti:
        iduri += str(_participant["id_participant"])
        iduri += " "
    return iduri

def tipareste_participanti(lista_toti):
    are_ceva = 0
    print("")
    for _participant in lista_toti:
        print("Concurentul identificat cu id-ul "+str(_participant["id_participant"])+" are scorul "+str(_participant["scor"])+".")
        are_ceva += 1
    if are_ceva == 0:
        print("\nNu exista participanti!\n")

def functionalitate2a(lista_toti, care):
    #functie care sterge scorul pentru un participant anume, daca se id-ul sau
    #input:  lista_toti - lista de participanti unic identificabili prin id_participant
    #        care - int > 0
    #rezultat: me modifica lista cu toti participantii, adica la scorul participantului dat se pune 0
    ce=exista_participant(lista_toti, care)
    for _participant in lista_toti:
        if _participant["id_participant"]==care:
            _participant["scor"]=0
    return ce

def functionalitate2b(lista_toti, poz1, poz2,id_de_la_care,scoruri_sterse):
    #functie care sterge scorul pentru un interval de participanti (pozitiile +1)
    #se verifica daca poz1<=poz2 si daca poz2<=len(lista_toti)
    #input:  lista_toti - lista de participanti unic identificabili prin id_participant
    #        poz1, poz2 - int
    #output: -1 , daca nu e interval , altfel se pune 0 la scorul participantilor din interval
    if este_interval(poz1, poz2) and poz1>=1 and poz2<=len(lista_toti):
        k = 0
        for _participant in lista_toti:
            k += 1
            if k>=poz1 and k<=poz2:
                id_de_la_care.append(_participant["id_participant"])
                scoruri_sterse.append(_participant["scor"])
                _participant["scor"]=0
    else:
        return -1

def functionalitate2c(lista_toti, care, scor,scor_initial):
    #functie care inlocuieste scorul pentru un participant anume, daca se id-ul sau
    #input:  lista_toti - lista de participanti unic identificabili prin id_participant
    #        care - int > 0
    #        scor - float >= 10
    #rezultat: se modifica lista cu toti participantii, adica la scorul participantului 
    #dat se pune noul scor
    scor_initial = exista_participant(lista_toti, care)
    scor_total = 0
    for i in range(len(scor)):
        scor_total += scor[i]
    for _participant in lista_toti:
        if _participant["id_participant"]==care:
            _participant["scor"]=scor_total
    return scor_initial

def functionalitate2c2(lista_toti, care, scor):
    #functie care inlocuieste scorul pentru un participant anume, daca se id-ul sau
    #input:  lista_toti - lista de participanti unic identificabili prin id_participant
    #        care - int > 0
    #        scor - float >= 10
    #rezultat: se modifica lista cu toti participantii, adica la scorul participantului 
    #dat se pune noul scor
    for _participant in lista_toti:
        if _participant["id_participant"]==care:
            _participant["scor"]=scor

def functionalitate3a(lista_toti, scor_dat):
    #functie de tipul interactiune cu utilizatorul, care cere sa fie introdus un scor pentru care sa se apeleze o functie care sa returneze un sir cu
    #toti participantii care au scor mai mic decat respectivul
    #input:  lista_toti - lista de participanti unic identificabili prin id_participant
    #        scor_dat - float >= 10
    #output: sir - un vector cu id-urile participantilor, cu spatiu intre ele
    sir = []
    for _participant in lista_toti:
        if _participant["scor"]<scor_dat:
            sir.append(_participant)
    return sir

def functionalitate3b(lista_toti):
    #functie care tipareste participantii ordonat crescator
    #input: lista_toti - lista de participanti unic identificabili prin id_participant
    #output: l - lista sortata dupa scor
    l = lista_toti[:]
    isSort = False
    while (not isSort):
        isSort = True
        for i in range(0, len(l) - 1):
            if (get_scor(l[i]) > get_scor(l[i + 1])):
                aux = l[i]
                l[i] = l[i + 1]
                l[i + 1] = aux
                isSort = False
    return l
        
def functionalitate3c(lista_toti, scor_dat):
    #functie care tipareste participantii ordonat crescator si care au scor mai mare decat unul dat
    #input: lista_toti - lista de participanti unic identificabili prin id_participant
    #       scor_dat >= 10
    #output: l - lista sortata dupa scor
    l = []
    for _participant in lista_toti:
        if _participant["scor"]>scor_dat:
            l.append(_participant)
    isSort = False
    while (not isSort):
        isSort = True
        for i in range(0, len(l) - 1):
            if (get_scor(l[i]) > get_scor(l[i + 1])):
                aux = l[i]
                l[i] = l[i + 1]
                l[i + 1] = aux
                isSort = False
    return l
    
        
        
def functionalitate4a(lista_toti,poz1,poz2):
    #functionalitate care calculeaza media scorurilor pentru un interval dat
    #verifica si daca e interval
    #input:  lista_toti - lista de participanti unic identificabili prin id_participant
    #        poz1, poz2 - int
    #output: media - float cu media, daca este interval, 
    #        -1, altfel
    if este_interval(poz1, poz2) and poz1>=1 and poz2<=len(lista_toti):#se verifica sa avem interval, din poz1 si poz2, respectiv ultima poz sa incapa in lista
        media = 0.0
        lg = poz2 - poz1 + 1
        al_catelea = 0
        for _participant in lista_toti:
            al_catelea += 1
            if al_catelea>=poz1 and al_catelea<=poz2:
                media += _participant["scor"]
        media = media/lg
        return media
    else:
        return -1


def functionalitate4b(lista_toti,poz1,poz2):
    #functie care returrneaza un sir cu id-urile participantilor cu scor minim
    #verifica si daca e interval
    #input: lista_toti - lista participantilor
    #       poz1,poz2 - int
    #output: lista_cu_scor_minim - un sir participantii cautati
    #      -1, daca nu e interval  
    if este_interval(poz1, poz2) and poz1 >=1 and poz2<=len(lista_toti):
        lista_cu_scor_minim = []
        al_catelea = 0
        scor_minim = 100
        for _participant in lista_toti:
            al_catelea += 1
            if al_catelea>=poz1 and al_catelea<=poz2 and _participant["scor"]<scor_minim:
                scor_minim = _participant["scor"]
        al_catelea = 0
        for _participant in lista_toti:
            al_catelea += 1
            if al_catelea>=poz1 and al_catelea<=poz2 and _participant["scor"]==scor_minim:
                lista_cu_scor_minim.append(_participant)
        return lista_cu_scor_minim
    return -1


def functionalitate4c(lista_toti,poz1,poz2):
    #functie care returrneaza un sir cu id-urile participantilor cu scor multiplu de 10
    #verifica si daca e interval
    #input: lista_toti - lista participantilor
    #       poz1,poz2 - int
    #output: lista_participanti_scor_multiplu_de_zece - un sir cu id-uri, daca exista
    #      -1, daca nu e interval  
    if este_interval(poz1, poz2) and poz1 >=1 and poz2<=len(lista_toti):
        al_catelea = 0
        lista_participanti_scor_multiplu_de_zece = []
        for _participant in lista_toti:
            al_catelea += 1
            if al_catelea>=poz1 and al_catelea<=poz2 and _participant["scor"]%10==0:
                lista_participanti_scor_multiplu_de_zece.append(_participant)
        return lista_participanti_scor_multiplu_de_zece
    return -1 #nu e interval

def functionalitate5a(lista_toti,scor_dat):
    #functie care face filtrarea, adica daca scorul unui participant nu este multiplul unui numar dat
    #input:  lista_toti - lista de participanti unic identificabili prin id_participant
    #        scor_dat - float
    #output: -1, daca nu se face filtrarea
    #        l - lista cu pozitiile unde s-a filtrat
    lista_filtru=lista_toti[:]
    lg = 0
    l = []
    for _participant in lista_filtru:
        lg+=1
        if _participant["scor"]%scor_dat != 0:
            l.append(lg)
    if len(l)==0:
        return -1
    return l

def functionalitate5b(lista_toti,scor_dat):
    #functie care face filtrarea, adica daca e mai mic decat scorul dat
    #input:  lista_toti - lista de participanti unic identificabili prin id_participant
    #        scor_dat - float
    #output: -1, daca nu se face filtrarea
    #        l - lista cu pozitiile unde s-a filtrat
    lista_filtru=lista_toti[:]
    lg = 0
    l = []
    for _participant in lista_filtru:
        lg+=1
        if _participant["scor"]<scor_dat:
            l.append(lg)
    if len(l)==0:
        return -1
    return l

def functionalitate6(lista_toti, stack):
    #functie de undo, care afiseaza lista cum era inainte de ultima adaugare a unui participant sau, respectiv, inainte de ultima modificare a scorurilor participantilor existeni
    #input: lista_toti - lista cu participantii
    #       stack - lista care simuleaza o stiva, in care se gasesc alte liste, care au pe prima pozitie coduri de la 1 la 4
    #semnificatie coduri:
    #                    sterge_participantul_adaugat = ultima data s-a adaugat un participant
    #                    refa_scorul_de_dinainte = ultima data s-a sters scorul unui participant
    #                    sterge_scoruri_din_interval = ultima data s-au sters scorurile participantilor dintr-un interval dat
    #                    modifica_scorul = ultima data s-a modificat scorul unui participant
    #output: reface lista_toti la forma pe care o avea inainte de ultima adaugare/stergere/modificare, iar stack revine la actiunea de dinaintea ultimei, samd
    ultima = stack[-1]
    if ultima[0]=="sterge_participantul_adaugat":
        lista_toti.pop()
        stack.pop()
    elif ultima[0]=="refa_scorul_de_dinainte":
        functionalitate2c2(lista_toti, ultima[1], ultima[2])
        stack.pop()
    elif ultima[0]=="sterge_scoruri_din_interval":
        i = ultima[1]
        id_uri_de_adaugat = ultima[3]
        scoruri_de_adaugat = ultima[4]
        ordi = 0
        while i <= ultima[2]:
            functionalitate2c2(lista_toti, id_uri_de_adaugat[ordi],scoruri_de_adaugat[ordi])
            ordi += 1
            i += 1
        stack.pop()
    elif ultima[0]=="modifica_scorul":
        functionalitate2c2(lista_toti, ultima[1],ultima[2])
        stack.pop()
