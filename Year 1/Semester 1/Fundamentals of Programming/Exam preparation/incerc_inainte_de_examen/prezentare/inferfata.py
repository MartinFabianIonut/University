from erors.exceptii import ValidError, RepoError
class Console(object):

    
    def __init__(self, srv):
        self.__srv = srv
    
    
    def meniu(self):
        print("Alegeti una dintre optiunile de mai jos:\n\t1.Adauga o noua piesa.\n\t2.Modifica piesa.\n\t3.Creeaza aleator piese.\n\t4.Exporta")
        print("\t5.Afiseaza piesele.")

    def __ui_adauga(self):
        titlu = input("Introdu titlul: ")
        regizor = input("Introdu numele regizorului: ")
        try:
            gen = input("Introdu genul (dintre urmatoarele: Drama, Comedie, Satira, Altele): ")
            genuri = ["Drama","Comedie","Satira","Altele"]
            ok = False
            for g in genuri:
                if gen == g:
                    ok = True 
            if not ok:
                raise ValueError("gen")
            durata = int(input("Introdu durata (numar intreg pozitiv): "))
        except ValueError as ve:
            if str(ve) == "gen":
                print("Genul nu este dintre cele specificate!\n")
            else:
                print("Durata nu e numar pozitiv!\n")
        self.__srv.adauga_piesa(titlu,regizor,gen,durata)
        print("Piesa adaugata cu succes!\n")
    
    
    def __ui_modifica(self):
        pass
    
    
    def __ui_creeaza(self):
        try:
            cate = int(input("Introdu cate piese vrei sa generezi automat: "))
        except:
            print("Invalid!\n")
        self.__srv.genereaza(cate)
    
    
    def __ui_exporta(self):
        nume = input("Nume: ")
        nume+=".txt"
        self.__srv.exporta(nume)
    
    
    def __ui_print(self):
        piese = self.__srv.get_all() 
        for p in piese:
            print(p)
        print("")
    
    
    def run(self):
        while True:
            self.meniu()
            cmd = input(">>>")
            if cmd == "exit":
                return
            elif cmd == "":
                continue
            elif cmd=="1":
                try:
                    self.__ui_adauga()
                except ValidError as ve:
                    print("Eroare la validare:" + str(ve))
            elif cmd == "2":
                try:
                    self.__ui_modifica()
                except ValidError as ve:
                    print("Eroare la validare:" + str(ve))
                except RepoError as re:
                    print("Eroare la repozitoriu:" + str(re))
            elif cmd == "3":
                self.__ui_creeaza()
            elif cmd == "4":
                self.__ui_exporta()
            elif cmd == "5":
                self.__ui_print()
            else:
                print("Comanda invalida!")
                
    
    



