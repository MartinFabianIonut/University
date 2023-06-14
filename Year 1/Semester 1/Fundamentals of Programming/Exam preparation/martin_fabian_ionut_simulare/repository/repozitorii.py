from domain.entitati import Carte, Imprumut

class RepoCarte(object):
    
    def __init__(self):
        #constructorul meu - creez lista vida de carti
        self._carti = []
    
    def __len__(self):
        #functie care returneaza lungimea sirului de carti
        return len(self._carti)
    
    def get_all(self):
        #functie care returneaza lista tuturor cartilor
        return self._carti


class FileRepoCarte(RepoCarte):
    
    
    def __init__(self,file_path):
        self.__file_path = file_path
        RepoCarte.__init__(self)
        

    def __read_all_from_file(self):
        #functie care citeste cartile din fisier si le adauga in lista de carti
        self._carti = []
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip()
                parti = linie.split(",")
                if len(parti) == 4:
                    id_carte = int(parti[0])
                    nume_carte = parti[1]
                    autor = parti[2]
                    an = int(parti[3])
                    carte = Carte(id_carte,nume_carte,autor,an)
                    self._carti.append(carte)
    
    
    def __len__(self):
        self.__read_all_from_file()
        return RepoCarte.__len__(self)
    
    def get_all(self):
        #suprascrierea functiei care returneaza lista tuturor cartilor
        self.__read_all_from_file()
        return RepoCarte.get_all(self)
    
    



class RepoImprumut(object):
    
    def __init__(self):
        self._imprumuturi = []
    
    def __len__(self):
        #functie care returneaza lungimea sirului de imprumuturi
        return len(self._imprumuturi)
    
    def get_all(self):
        #functie care returneaza sirul de imprumuturi
        return self._imprumuturi


class FileRepoImprumut(RepoImprumut):
    
    
    def __init__(self,file_path):
        self.__file_path = file_path
        RepoImprumut.__init__(self)
    

    def __read_all_from_file(self):
        #functie care citeste imprumuturile din fisier si le adauga in lista de imprumuturi
        self._imprumuturi = []
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip()
                parti = linie.split(",")
                if len(parti) == 4:
                    id_imprumut = int(parti[0])
                    id_carte = int(parti[1])
                    data_imprumut = parti[2]
                    durata_imprumut = int(parti[3])
                    imprumut = Imprumut(id_imprumut,id_carte,data_imprumut,durata_imprumut)
                    self._imprumuturi.append(imprumut)
    
    
    def __len__(self):
        #suprascrierea functiei care returneaza lungimea sirului de imprumuturi
        self.__read_all_from_file()
        return RepoImprumut.__len__(self)
    
    def get_all(self):
        #suprascrierea functiei de returnare a tuturor imprumuturilor
        self.__read_all_from_file()
        return RepoImprumut.get_all(self)



