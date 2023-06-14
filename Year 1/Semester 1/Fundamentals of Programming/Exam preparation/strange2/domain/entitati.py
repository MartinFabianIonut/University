class Concurent(object):
    
    
    def __init__(self, id_concurent, nume, tara, data_nasterii):
        self.__id_concurent = id_concurent
        self.__nume = nume
        self.__tara = tara
        self.__data_nasterii = data_nasterii

    def get_id_concurent(self):
        return self.__id_concurent


    def get_nume(self):
        return self.__nume


    def get_tara(self):
        return self.__tara


    def get_data_nasterii(self):
        return self.__data_nasterii


    def set_nume(self, value):
        self.__nume = value


    def set_tara(self, value):
        self.__tara = value


    def set_data_nasterii(self, value):
        self.__data_nasterii = value
    
    def __eq__(self, altul):
        return self.__id_concurent == altul.__id_concurent
    
    def __str__(self):
        return f"Id: {str(self.__id_concurent)}, Nume: {self.__nume}, Tara: {self.__tara}, Data nasterii: {self.__data_nasterii}"
    
    



class Participare(object):
    
    
    def __init__(self, cod, id_concurent, punctaj):
        self.__cod = cod
        self.__id_concurent = id_concurent
        self.__punctaj = punctaj

    def get_cod(self):
        return self.__cod


    def get_id_concurent(self):
        return self.__id_concurent


    def get_punctaj(self):
        return self.__punctaj


    def set_punctaj(self, value):
        self.__punctaj = value
        

    
    



class ObjDTO(object):
    
    
    def __init__(self, tara, punctaj_total):
        self.__tara = tara
        self.__punctaj_total = punctaj_total

    def get_tara(self):
        return self.__tara


    def get_punctaj_total(self):
        return self.__punctaj_total


    def set_tara(self, value):
        self.__tara = value


    def set_punctaj_total(self, value):
        self.__punctaj_total = value

    def __str__(self):
        return f"Tara {self.__tara} are punctajul total: {str(self.__punctaj_total)}."
    



