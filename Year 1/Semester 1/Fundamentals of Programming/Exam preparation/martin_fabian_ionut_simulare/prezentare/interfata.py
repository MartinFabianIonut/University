from erors.exceptii import RepositoryError

class Consola(object):

    
    def __init__(self, srv_carte, srv_imprumut):
        self.__srv_carte = srv_carte
        self.__srv_imprumut = srv_imprumut
    
    
    
    
    def __meniu(self):
        print("Meniu\n\nAlege o functionaltiate:\n\t1. Scrie 1 pentru afisarea cartilor care se termina cu un sir de caractere citit, ordonate dupa an.\n\t2. Scrie 2 pentru afisarea imprumuturilor de x zile, x nr intreg citit de la tastatura.")
    
    
    def __ui_se_termina_cu(self):
        sir = input("Introdu sirul de caractere cu care sa se termine titlul: ")
        carti_de_afisat = self.__srv_carte.se_termina_cu(sir)
        if len(carti_de_afisat)>0:
            print("")
            for carte in carti_de_afisat:
                print(carte)
            print("")
    
    
    def __ui_durata_x(self):
        try:
            durata = int(input("Scrie durata - un numar intreg > 0: ")) #aici fac validarea duratei, care trebuie sa fie atat un numar
            assert(durata>0) #cat si sa fie pozititv
        except:
            print("\nCe ati introdus e invalid!\n")
            return
        imprumuturi = self.__srv_imprumut.durata_x(durata)
        if len(imprumuturi)>0:
            print("")
            for imprumut in imprumuturi:
                print(imprumut)
            print("")
    
    
    def run(self):
        while True:
            self.__meniu()
            cmd = input(">>>")
            if cmd == "exit":
                return
            elif cmd == "":
                continue
            elif cmd == "1":
                try:
                    self.__ui_se_termina_cu()
                except RepositoryError as re:
                    print("\nEroare la nivelul de repozitorii: " + str(re))
            elif cmd == "2":
                try:
                    self.__ui_durata_x()
                except RepositoryError as re:
                    print("\nEroare la nivelul de repozitorii: " + str(re))
            else:
                print("\nComanda invalida!\n")
        
    



