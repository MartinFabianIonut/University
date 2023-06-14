from erors.exceptii import ValidError, RepoError

class Console(object):

    
    def __init__(self, srv):
        self.__srv = srv
    
    
    
    def __ui_tiparire_initiala(self):
        evenimente = self.__srv.ziua_curenta()
        for eveniment in evenimente:
            print(eveniment) 
        print("")
    
    
    def __meniu(self):
        print("Aplicatie examen Martin - alege o optiune:\n\t1. Adauga eveniment.\n\t2. Filtreaza dupa data.\n\t3. Export de evenimente.\n")
    
    
    def __ui_adauga(self):
        try:
            ide = int(input("Introdu id eveniment: "))
        except:
            print("Id invalid!\n")
        data = input("Introdu data (format dd.mm.yyyy): ")
        ora = input("Introdu ora (format hh.mm): ")
        descriere = input("Introdu descriere: ")
        self.__srv.adauga(ide,data,ora,descriere)
        print("\nEveniment introdus cu succes!\n")
    
    
    def __ui_filtrare_dupa_data(self):
        data = input("Introdu data pentru care vrei sa vezi evenimentele: ")
        afisez = self.__srv.filtrare(data)
        for eveniment in afisez:
            print(eveniment)
        print("")
    
    
    def __export(self):
        nume = input("Introdu numele fisierului in care vrei sa exporti: ")
        nume = nume + ".txt"
        sir = input("Introdu un sir care sa existe in descriere: ")
        self.__srv.export(nume,sir)
    
    
    def run(self):
        self.__ui_tiparire_initiala()
        while True:
            self.__meniu()
            cmd = input(">>>")
            if cmd == "exit":
                return
            elif cmd == "":
                continue
            elif cmd == "1":
                try:
                    self.__ui_adauga()
                except ValidError as ve:
                    print("Eroare la validare:" + str(ve))
                except RepoError as re:
                    print("Eroare la nivelul repozitoriului de date:" + str(re))
            elif cmd == "2":
                try:
                    self.__ui_filtrare_dupa_data()
                except RepoError as re:
                    print("Eroare la nivelul repozitoriului de date:" + str(re))
            elif cmd  == "3":
                try:
                    self.__export()
                except RepoError as re:
                    print("Eroare la nivelul repozitoriului de date:" + str(re))
    



