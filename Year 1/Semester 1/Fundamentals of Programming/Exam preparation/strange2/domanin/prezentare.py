from erori.exceptii import RepoError
class Consola(object):
    
    
    def __init__(self, srv_concurent, srv_participare):
        self.__srv_concurent = srv_concurent
        self.__srv_participare = srv_participare

    
    def __meniu(self):
        print("Meniu\n\nAlege ce functionalitate vrei:\n1. Scrie 1 pentru afisarea concurentilor nascuti dupa un an dat.\n2. Scrie 2 pentru afisarea clasamentului pe tari.\n3. Scrie exit pentru a iesi din program.")
    
    
    def __ui_nascuti_dupa(self):
        try:
            an = int(input("Introdu anul: "))
            dupa_an = self.__srv_concurent.nascuti_dupa_an(an)
            if len(dupa_an)>0:
                for concurent in dupa_an:
                    print(concurent)
            else:
                print("\nNu exista concurenti nascuti dupa anul introdus!\n")     
        except RepoError as re:
            print("Eroare la repo: " + str(re))
        except:
            print("\nNu ati introdus o valoare corecta! Adica numar intreg.\n")
    
    
    def __ui_clasament(self):
        top = self.__srv_participare.clasament()
        print("")
        for tara in top:
            print(tara)
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
                    self.__ui_nascuti_dupa()
                except RepoError as re:
                    print("Eroare la repo: " + str(re))
            elif cmd == "2":
                self.__ui_clasament()
            else:
                print("\nComanda invalida!\n")
    
    
    
    



