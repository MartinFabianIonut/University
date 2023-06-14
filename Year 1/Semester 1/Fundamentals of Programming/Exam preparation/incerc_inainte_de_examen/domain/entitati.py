class Piesa(object):
    
    
    def __init__(self, titlu, regizor, gen, durata):
        self.__titlu = titlu
        self.__regizor = regizor
        self.__gen = gen
        self.__durata = durata

    def get_titlu(self):
        return self.__titlu


    def get_regizor(self):
        return self.__regizor


    def get_gen(self):
        return self.__gen


    def get_durata(self):
        return self.__durata


    def set_gen(self, value):
        self.__gen = value


    def set_durata(self, value):
        self.__durata = value

    def __eq__(self, alta):
        return self.get_titlu()==alta.get_titlu() and self.get_regizor()==alta.get_regizor()
    
    def __str__(self):
        return f"Piesa cu titlul {self.__titlu}, in regia lui {self.__regizor} -> Genul: {self.__gen}, Durata: {str(self.__durata)} (in ore)."
    



