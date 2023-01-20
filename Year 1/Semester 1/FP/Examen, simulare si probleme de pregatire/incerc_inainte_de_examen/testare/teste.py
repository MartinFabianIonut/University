from domain.entitati import Piesa
class Teste(object):
    
    
    def __test_creeaza_piesa(self):
        titlu = "Da"
        regizor = "Da"
        gen = "Drama"
        durata = 4
        piesa = Piesa(titlu,regizor,gen,durata)
        assert piesa.get_titlu()==titlu
        assert piesa.get_regizor()==regizor
        assert piesa.get_gen()==gen
        assert piesa.get_durata()==durata
        
    
    
    def run_all_tests(self):
        self.__test_creeaza_piesa()
    
    



