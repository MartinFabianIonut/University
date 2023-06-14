from erori.exceptii import RepoError
from domain.entitati import Pictura
class RepoPicturi(object):
    
    def __init__(self):
        self._picturi = []
    
    def __len__(self):
        return len(self._picturi)
        
    def get_all(self):
        return self._picturi
    
class FileRepoPicturi(RepoPicturi):
    
    def __init__(self, file_path):
        self.__file_path = file_path
        RepoPicturi.__init__(self)
        

    def __read_all_from_file(self):
        self._picturi = []
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip() 
                parti = linie.split(",")
                if len(parti)==4:
                    id_pictura = int(parti[0])
                    nume = parti[1]
                    nume_pictor = parti[2]
                    an = int(parti[3])
                    pictura = Pictura(id_pictura,nume,nume_pictor,an)
                    self._picturi.append(pictura)
            
    
    
    def __len__(self):
        self.__read_all_from_file()
        return RepoPicturi.__len__(self)
    
    def get_all(self):
        self.__read_all_from_file()
        return RepoPicturi.get_all(self)


