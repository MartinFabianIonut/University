class Carte(object):
    
    
    def __init__(self, id_carte, nume_carte, autor, an): #am denumit id-ul cartii id_carte, pt ca id e o variabila prea comuna, identificata uneori de Python drept ceva autoimplementat
        self.__id_carte = id_carte
        self.__nume_carte = nume_carte
        self.__autor = autor
        self.__an = an

    def get_id_carte(self):
        return self.__id_carte


    def get_nume_carte(self):
        return self.__nume_carte


    def get_autor(self):
        return self.__autor


    def get_an(self):
        return self.__an


    def set_nume_carte(self, value):
        self.__nume_carte = value


    def set_autor(self, value):
        self.__autor = value


    def set_an(self, value):
        self.__an = value

    def __eq__(self, alta):
        return self.__id_carte == alta.__id_carte
    
    def __str__(self):
        return f"Id: {str(self.__id_carte)}, Titlu: {self.__nume_carte}, Autor: {self.__autor}, Anul: {str(self.__an)}."
    



class Imprumut(object):
    
    
    def __init__(self, id_imprumut, id_carte, data_imprumut, durata_imprumut):
        self.__id_imprumut = id_imprumut
        self.__id_carte = id_carte
        self.__data_imprumut = data_imprumut
        self.__durata_imprumut = durata_imprumut

    def get_id_imprumut(self):
        return self.__id_imprumut


    def get_id_carte(self):
        return self.__id_carte


    def get_data_imprumut(self):
        return self.__data_imprumut


    def get_durata_imprumut(self):
        return self.__durata_imprumut


    def set_id_carte(self, value):
        self.__id_carte = value


    def set_data_imprumut(self, value):
        self.__data_imprumut = value


    def set_durata_imprumut(self, value):
        self.__durata_imprumut = value

    def __eq__(self, altul):
        return self.__id_imprumut == altul.__id_imprumut
    
    def __str__(self):
        return f"Id imprumut: {str(self.__id_imprumut)}, Id carte: {str(self.__id_carte)}, Data: {self.__data_imprumut}, Durata (in zile): {str(self.__durata_imprumut)}."
    
    



