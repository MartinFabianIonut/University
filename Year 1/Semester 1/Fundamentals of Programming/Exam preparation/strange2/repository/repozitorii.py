from domain.entitati import Concurent, Participare
from erori.exceptii import RepoError

class RepoConcurenti(object):
    
    def __init__(self):
        self._concurenti = []
    
    def __len__(self):
        return len(self._concurenti)
    
    def get_all(self):
        return self._concurenti
    
    def cauta_concurent(self, id_concurent):
        ok = True
        for concurent in self._concurenti:
            if concurent.get_id_concurent() == id_concurent:
                return concurent
        if ok:
            raise RepoError("\nNu exsita concurent cu asemenea id!\n")


class FileRepoConcurenti(RepoConcurenti):
    
    
    def __init__(self, file_path):
        self.__file_path = file_path
        RepoConcurenti.__init__(self)
        

    def __read_all_from_file(self):
        self._concurenti = []
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            for linie in linii:
                linie= linie.strip()
                parti = linie.split(",")
                if len(parti) == 4:
                    id_concurent = int(parti[0])
                    nume = parti[1]
                    tara = parti[2]
                    data_nasterii = parti[3]
                    concurent = Concurent(id_concurent,nume,tara,data_nasterii)
                    self._concurenti.append(concurent)
    
    
    def __len__(self):
        self.__read_all_from_file()
        return RepoConcurenti.__len__(self)
    
    def get_all(self):
        self.__read_all_from_file()
        return RepoConcurenti.get_all(self)
    
    def cauta_concurent(self, id_concurent):
        self.__read_all_from_file()
        return RepoConcurenti.cauta_concurent(self, id_concurent)
    
    


class RepoParticipari(object):
    
    def __init__(self):
        self._participari = []
    
    def __len__(self):
        return len(self._participari)
    
    def get_all(self):
        return self._participari
    


class FileRepoParticipari(RepoParticipari):
    
    
    def __init__(self, file_path):
        self.__file_path = file_path
        RepoParticipari.__init__(self)
    
    def __read_all_from_file(self):
        self._participari = []
        with open(self.__file_path, "r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip()
                parti = linie.split(",")
                if len(parti) == 3:
                    cod = int(parti[0])
                    id_concurent = int(parti[1])
                    punctaj = float(parti[2])
                    participare  = Participare(cod,id_concurent,punctaj)
                    self._participari.append(participare)
    
    def __len__(self):
        self.__read_all_from_file()
        return RepoParticipari.__len__(self)
    
    def get_all(self):
        self.__read_all_from_file()
        return RepoParticipari.get_all(self)
    



