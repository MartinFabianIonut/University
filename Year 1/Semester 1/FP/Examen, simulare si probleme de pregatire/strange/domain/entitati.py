class Pictura(object):
    
    
    def __init__(self, id_pictura, nume, nume_pictor, an):
        self.__id_pictura = id_pictura
        self.__nume = nume
        self.__nume_pictor = nume_pictor
        self.__an = an

    def get_id_pictura(self):
        return self.__id_pictura


    def get_nume(self):
        return self.__nume


    def get_nume_pictor(self):
        return self.__nume_pictor


    def get_an(self):
        return self.__an


    def set_nume(self, value):
        self.__nume = value


    def set_nume_pictor(self, value):
        self.__nume_pictor = value


    def set_an(self, value):
        self.__an = value

    def __eq__(self, altul):
        return self.__id_pictura == altul.__id_pictura
    
    def __str__(self):
        return "Id: " + str(self.__id_pictura) + ", Titlu: " + self.__nume + ", Autor: " + self.__nume_pictor + ", Anul crearii: " + str(self.__an) + "."
    



class PictDTO(object):
    
    
    def __init__(self, nume_pictor, nume, an):
        self.__nume_pictor = nume_pictor
        self.__nume = nume
        self.__an = an

    def get_nume_pictor(self):
        return self.__nume_pictor


    def get_nume(self):
        return self.__nume


    def get_an(self):
        return self.__an


    def set_nume(self, value):
        self.__nume = value


    def set_an(self, value):
        self.__an = value
        
    def __str__(self):
        return f"Pictorul {self.__nume_pictor}, cu tabloul intitulat {self.__nume}, din anul {str(self.__an)}."
    
    



