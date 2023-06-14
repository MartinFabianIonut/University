class Eveniment(object):
    
    
    def __init__(self, ide, data, ora, descriere):
        self.__ide = ide
        self.__data = data
        self.__ora = ora
        self.__descriere = descriere
    
    def get_ide(self):
        return self.__ide
    
    def get_data(self):
        return self.__data


    def get_ora(self):
        return self.__ora


    def get_descriere(self):
        return self.__descriere


    def set_data(self, value):
        self.__data = value


    def set_ora(self, value):
        self.__ora = value


    def set_descriere(self, value):
        self.__descriere = value

    def __eq__(self, altul):
        return self.__ide == altul.get_ide()
    
    def __str__(self):
        return f"Evenimentul {str(self.__ide)} are loc in data {self.__data}, de la ora {self.__ora}. Descriere: {self.__descriere}"
    
    



