'''
Created on 18 nov. 2021

@author: marti
'''

class NoteDTO:
    
    def __init__(self,id_nota,id_student,nrlab_nrpb,nota):
        self.__id_nota = id_nota
        self.__id_student = id_student
        self.__student = None
        self.__nrlab_nrpb = nrlab_nrpb
        self.__lab = None
        self.__nota = nota

    def get_id_nota(self):
        return self.__id_nota


    def get_id_student(self):
        return self.__id_student


    def get_student(self):
        return self.__student


    def get_nrlab_nrpb(self):
        return self.__nrlab_nrpb


    def get_lab(self):
        return self.__lab


    def get_nota(self):
        return self.__nota


    def set_student(self, value):
        self.__student = value


    def set_lab(self, value):
        self.__lab = value
        
    def set_nota_lab(self, value):
        self.__nota = value
        
    def __str__(self):
        #return "Nota cu id: " + str(self.__id_nota) + " -> Student:  " + self.__student.get_nume()+ " -> problema: "+ self.__nrlab_nrpb+ " -> nota: "+str(self.__nota)+"."
        return self.__student.get_nume()+ " are nota: "+str(self.__nota)+"."
    
    def __eq__(self, altul):
        return self.__id_nota == altul.__id_nota
    
class StudenticuNoteDTO:
    
    def __init__(self,nrlab_nrpb,lista_studenti):
        self.__nrlab_nrpb = nrlab_nrpb
        self.__lista_studenti = lista_studenti
        
    def __str__(self):
        st = ""
        st += "Laboratorul " + self.__nrlab_nrpb + " are urmatoarea/rele nota/e:\n"
        for student in self.__lista_studenti:
            st += "\t"+"Studentul " + str(student) + "\n"
        return st



