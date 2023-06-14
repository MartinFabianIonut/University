class Student(object):
    
    
    def __init__(self, id_student, nume, grupa):
        self.__id_student = id_student
        self.__nume = nume
        self.__grupa = grupa

    def get_id_student(self):
        return self.__id_student


    def get_nume(self):
        return self.__nume


    def get_grupa(self):
        return self.__grupa


    def set_nume(self, value):
        self.__nume = value


    def set_grupa(self, value):
        self.__grupa = value
        
    def __eq__(self, altul):
        return self.__id_student == altul.__id_student
    
    def __str__(self):
        return "Id: " + str(self.__id_student) + " -> Nume: " + self.__nume + ", din grupa " + str(self.__grupa) + "."



class Laborator(object):
    
    
    def __init__(self, nrlab_nrpb, descriere, deadline):
        self.__nrlab_nrpb = nrlab_nrpb
        self.__descriere = descriere
        self.__deadline = deadline

    def get_nrlab_nrpb(self):
        return self.__nrlab_nrpb


    def get_descriere(self):
        return self.__descriere


    def get_deadline(self):
        return self.__deadline

    def set_descriere(self, value):
        self.__descriere = value


    def set_deadline(self, value):
        self.__deadline = value
        
    def __eq__(self, altul):
        return self.__nrlab_nrpb == altul.__nrlab_nrpb
    
    def __str__(self):
        return "Numar laborator_numar problema: " + self.__nrlab_nrpb + " Descrierea: " + self.__descriere + " Deadline-ul: " + str(self.__deadline[0]) + "." + str(self.__deadline[1]) + "." + str(self.__deadline[2]) + "."


class Note(object):
    
    def __init__(self, id_nota, student, lab):
        self.__id_nota = id_nota
        self._student = student
        self._lab = lab
        self.__nota_pb = 0

    def get_id_nota(self):
        return self.__id_nota


    def get_student(self):
        return self._student


    def get_lab(self):
        return self._lab


    def get_nota_pb(self):
        return self.__nota_pb


    def set_student(self, value):
        self._student = value


    def set_lab(self, value):
        self._lab = value


    def set_nota_lab(self, value):
        self.__nota_pb = value

    def __eq__(self, altul):
        return self.__id_nota == altul.__id_nota
   
    def __str__(self):
        return "Nota cu id: " + str(self.__id_nota) + " -> Student:  " + self._student.get_nume() + " -> problema: " + self._lab.get_nrlab_nrpb() + " -> nota: " + str(self.__nota_pb)+ "."

class Note_DTO:
    
    def __init__(self, id_lab, medie_note_lab, nr_note):
        self.__id_lab = id_lab
        self.__medie_note_lab = medie_note_lab
        self.__nr_note = nr_note
        
    def get_id_lab(self):
        return self.__id_lab
        
    def get_medie_note_lab(self):
        return self.__medie_note_lab
    
    def get_nr_note(self):
        return self.__nr_note
    
    def __str__(self):
        return f"Laboratorul {self.__id_lab} are {str(self.__nr_note)} note, iar media notelor de la acest laborator este: {self.__medie_note_lab}!"
    