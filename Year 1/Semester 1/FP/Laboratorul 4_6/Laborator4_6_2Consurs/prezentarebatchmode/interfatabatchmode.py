from business.servicii import functionalitate1a, tipareste_participanti, functionalitate5a, functionalitate2a
from exceptii.erori import RepoError,ValidError

def runbatchmode():
    lista_toti = []
    while True:
        print("Scrie una sau mai multe dintre comenzile (cu virgula intre ele):")
        print("add + id + 10 scoruri, cu 1<=scor<=10 (se pune spatiu intre cele trei domenii), delete_score_one + id (spatiu intre ele), \
filter_not_multiple + nr, de tip float > 0 (spatiu intre ele), print")
        cmd = input(">>>")
        parti = []
        parti = cmd.split(",")
        for i in range (len(parti)):
            comanda_mea = parti[i].split(" ")
            if comanda_mea[0]=="add":
                try:
                    id_participant = int(comanda_mea[1])
                    if len(comanda_mea)==12:
                        scor = []
                        for i in range(10):
                            scor.append(float(comanda_mea[i+2]))
                        functionalitate1a(lista_toti, id_participant, scor)
                    elif len(comanda_mea)<12:
                        print("\nNu ati introdus sufieciente date (add + id + 10 scoruri) pentru participantul cu id: " + str(comanda_mea[1])+"\n")
                    else:
                        print("\nAti introdus prea multe date pentru participantul cu id: " + str(comanda_mea[1])+"\n")
                except ValidError as ve:
                    print(ve)
                except RepoError as re:
                    print(re)
                except:
                    print("Ati introdus un caracter sau un sir de caractere imposibil de convertit la numar intreg sau real! Reincercati!\n")
            elif comanda_mea[0]=="delete_score_one":
                try:
                    care = int(comanda_mea[1])
                    functionalitate2a(lista_toti, care)
                except ValidError as ve:
                    print("\nPentru id-ul "+str(comanda_mea[1])+", pe care l-ati introdus: ")
                    print(ve)
                except:
                    print("\nValoare invalida pentru id!\n")
            elif comanda_mea[0] == "filter_not_multiple":
                try:
                    scor_dat = float(comanda_mea[1])
                    assert(scor_dat>0 and scor_dat<=100)
                    lista_filtrata = functionalitate5a(lista_toti, scor_dat)
                    if lista_filtrata ==-1:
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
                                    print("Participantul " + str(_participant["id_participant"]) + " <-> 0.")
                            if oki == 0:
                                print("Participantul " + str(_participant["id_participant"]) + " <-> " + str(_participant["scor"]) + ".")
                except:
                    print("\nScorul introdus nu este corespunzator!\n")
            elif comanda_mea[0] == "print":
                tipareste_participanti(lista_toti)
            else:
                print("\nNu exista asemenea comanda! Verificati si observati ca ati scris: <<"+comanda_mea[0]+">>!\n")


