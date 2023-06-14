from erori.exceptii import ValidationError, RepoError
class Consola(object):
    
    
    def __init__(self, srv_picturi):
        self.__srv_picturi = srv_picturi

    
    def __meniu(self):
        print("Introdu una dintre urmatoarele:\n\ta. Cifra 1 pentru functionalitatea unu\n\tb. Cifra 2 pentru functionalitatea doi")
    
    
    def __ui_unu(self):
        try:
            sir = input("Introdu un sir de caractere de cautat in titlul picturii: ")
            picturi = self.__srv_picturi.unu(sir)
            if len(picturi)>0:
                print("")
                for pic in picturi:
                    print(pic)
                print("")
            else:
                print("Nu exista")
        except:
            print("Nu exista")
            return
    
    
    def __ui_doi(self):
        afisare = self.__srv_picturi.doi()
        print("")
        for af in afisare:
            print(af)
        print("")
    
    def run(self):
        while True:
            self.__meniu()
            cmd = input(">>>")
            if cmd == "exit":
                return
            if cmd == "":
                continue
            elif cmd == "1":
                try:
                    self.__ui_unu()
                except ValidationError as ve:
                    print("O eroare la validare: " + str(ve))
                except RepoError as re:
                    print("O eroare la repo: " + str(re))
            elif cmd == "2":
                try:
                    self.__ui_doi()
                except ValidationError as ve:
                    print("O eroare la validare: " + str(ve))
                except RepoError as re:
                    print("O eroare la repo: " + str(re))
            else:
                print("invalid")
            
    
    
    
    



