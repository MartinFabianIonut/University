from domain.entitati import Piesa
class RepoPiese(object):
    
    def __init__(self):
        self._piese = []
        
    def __len__(self):
        return len(self._piese)
        
    def adauga_piesa(self,piesa):
        for p in self._piese:
            if p == piesa:
                raise RepoPiese("Piesa existenta!\n")
        self._piese.append(piesa)
    
    def get_all(self):
        return self._piese

class FileRepoPiese(RepoPiese):
    
    
    def __init__(self, file_path):
        RepoPiese.__init__(self)
        self.__file_path = file_path
        

    def __append_all_to_file(self, piesa):
        with open(self.__file_path, "a") as f:
            f.write(piesa.get_titlu() + "," + piesa.get_regizor() + "," + piesa.get_gen() + "," + str(piesa.get_durata()) + "\n")
    
    
    def __read_all_from_file(self):
        self._piese = [] 
        with open(self.__file_path, "r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip()
                parti = linie.split(",")
                if len(parti)==4:
                    titlu = parti[0]
                    regizor = parti[1]
                    gen = parti[2]
                    durata = int(parti[3])
                    piesa = Piesa(titlu,regizor,gen,durata)
                    self._piese.append(piesa)
    
    def __len__(self):
        self.__read_all_from_file()
        return RepoPiese.__len__(self)
    
    def adauga_piesa(self, piesa):
        self.__read_all_from_file()
        RepoPiese.adauga_piesa(self, piesa)
        self.__append_all_to_file(piesa)
        
    def get_all(self):
        self.__read_all_from_file()
        return RepoPiese.get_all(self)
    

    def __write_all_to_file(self, nume):
        with open(nume, "w") as f:
            for piesa in self._piese:
                f.write(piesa.get_titlu() + "," + piesa.get_regizor() + "," + piesa.get_gen() + "," + str(piesa.get_durata()) + "\n")
    
    
    def exp(self,nume):
        self.__read_all_from_file()
        self.__write_all_to_file(nume)
    



