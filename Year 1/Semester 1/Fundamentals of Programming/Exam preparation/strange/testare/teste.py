from domain.entitati import Pictura
class Teste(object):
    
    
    def __run_test_unu(self):
        id_pictura = 1
        nume = "Tabloul meu"
        nume_pictor = "Martin Fabian"
        an = 2021
        pictura = Pictura(id_pictura,nume,nume_pictor,an)
        id_pictura2 = 1
        nume2 = "Impresionism"
        nume_pictor2 = "Martin Fabian"
        an2 = 2021
        pictura = Pictura(id_pictura2,nume2,nume_pictor2,an2)
    
    
    def run_all_tests(self):
        print("start")
        self.__run_test_unu()
        print("end")
    
    



