from domain.entitati import Eveniment
from erors.exceptii import RepoError

class RepoEvenimente(object):
    
    def __init__(self):
        self._evenimente = []
        
    def __len__(self):
        return len(self._evenimente)
    
    def get_all(self):
        return self._evenimente
    
    def adauga_ev(self,eveniment):
        #functie care adauga un eveniment in fisier, iar daca la ora nu avem 0 inainte de cifra, se adauga
        #input: eveniment
        #rezultatul se vede in fisier imediat
        #exceptii: exista deja evenimentul in fisier => RepoError
        ora = eveniment.get_ora()
        if int(ora[0])>0 and int(ora[0])<10:
            prefix = "0" + ora
        eveniment.set_ora(prefix)
        for even in self._evenimente:
            if even == eveniment:
                raise RepoError("Eveniment existent!\n")
        self._evenimente.append(eveniment)
        
    
        

class FileRepoEvenimente(RepoEvenimente):
    
    
    def __init__(self, file_path):
        RepoEvenimente.__init__(self)
        self.__file_path = file_path
        

    def __read_all_from_file(self):
        #functie care citeste si salveaza in memorie toate evenimentele din fisier
        self._evenimente = []
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip()
                parti = linie.split(",")
                if len(parti)==4:
                    ide = int(parti[0])
                    data = parti[1]
                    ora = parti[2]
                    descriere = parti[3]
                    eveniment = Eveniment(ide,data,ora,descriere)
                    self._evenimente.append(eveniment)
    
    
    def __len__(self):
        self.__read_all_from_file()
        return RepoEvenimente.__len__(self)
    
    def get_all(self):
        self.__read_all_from_file()
        return RepoEvenimente.get_all(self)
    

    def __append_all_to_file(self,eveniment):
        #functie care adauga in fisier un ev introdus
        #input: eveniment
        #rezultatul se vede in fisier imediat
        with open(self.__file_path,"a") as f:
            f.write(str(eveniment.get_ide()) + "," + eveniment.get_data() + "," + eveniment.get_ora() + "," + eveniment.get_descriere() + "\n")
    
    
    def adauga_ev(self, eveniment):
        #functie care adauga in fisier un ev introdus
        #input: eveniment
        #rezultatul se vede in fisier imediat
        self.__read_all_from_file()
        RepoEvenimente.adauga_ev(self, eveniment)
        self.__append_all_to_file(eveniment)
    

    def __write_all_to_file(self, de_adaugat, nume):
        #functie care scrie in fisier cu numele nume cu w, adica ce era initial dispare
        #input: de_adaugat - lista cu evenimentele de exportat
        #       nume - numele fisierului in care se scrie
        #rezultatul scrierii se vede in fisier imediat
        with open(nume,"w") as f:
            for ev in de_adaugat:
                f.write(str(ev.get_ide()) + "," + ev.get_data() + "," + ev.get_ora() + "," + ev.get_descriere() + "\n")
    
    
    def exporteaza(self,de_adaugat,nume):
        #functie care exporteaza intr-un fisier nou sau deja existent anumite evenimente, care contin un sir in descriere
        self.__read_all_from_file()
        self.__write_all_to_file(de_adaugat,nume)



