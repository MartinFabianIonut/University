from domain.entitati import Student,Laborator, Note, Note_DTO
import random
import string
from domain.dtos import StudenticuNoteDTO, NoteDTO
from sorters.sortari import InsertSort, CombSort

#------------------------------------------------------------------------Clasa de ServiceLaborator----------------------------------------------------------------------------------

class ServiceLaborator(object):
    
    
    def __init__(self, valid_lab, repo_lab):
        self.__valid_lab = valid_lab
        self.__repo_lab = repo_lab
    
    def adauga_lab(self,nrlab_nrpb,descriere,deadline):
        #functie care adauga laborator in repozitorii
        #input - nrlab_nrpb - str
        #        descriere - str
        #        deadline - lista cu zi, luna, an
        #rez: adaugarea
        lab = Laborator(nrlab_nrpb,descriere,deadline)
        self.__valid_lab.valideaza(lab)
        self.__repo_lab.adauga_lab(lab)
    
    def get_all_lab(self):
        #functie care returneaza toate laboratoarele
        return self.__repo_lab.get_all()

    def sterge_lab(self,nrlab_nrpb):
        #functie care sterge laboratorul dupa id, care e nrlab_nrpb
        #input: nrlab_nrpb - str
        #rez: sterge acel lab
        self.__valid_lab.valideaza_id(nrlab_nrpb)
        self.__repo_lab.sterge_lab_dupa_id(nrlab_nrpb)
    
    def modifica_descriere_lab(self,nrlab_nrpb,descriere_noua):
        #functie care modifica descrierea unui lab, dupa id  care e nrlab_nrpb
        #input - nrlab_nrpb - str
        #        descriere - str
        self.__valid_lab.valideaza_descriere_noua(descriere_noua)
        self.__repo_lab.modifica_descriere_lab(nrlab_nrpb,descriere_noua)
    
    def modifica_deadline_lab(self,nrlab_nrpb,deadline_nou):
        #functie care modifica deadline-ul unui lab, dupa id  care e nrlab_nrpb
        #input - nrlab_nrpb - str
        #        deadline - lista cu zi,luna,an
        self.__valid_lab.valideaza_deadline_nou(deadline_nou)
        self.__repo_lab.modifica_deadline_lab(nrlab_nrpb,deadline_nou)
        
    def cauta_lab(self,nrlab_nrpb):
        #funcite care cauta un laborator dupa id, care e nrlab_nrpb
        #input - nrlab_nrpb - str
        #output: laboratorul
        self.__valid_lab.valideaza_id(nrlab_nrpb)
        #return self.__repo_lab.cauta_lab_dupa_id(nrlab_nrpb)
        poz = 0
        return self.__repo_lab.cauta_lab_dupa_id_recursiv(nrlab_nrpb,poz)
    
    def get_id_random(self):
        id_meu = random.randint(1, 1000)
        return id_meu
    
    def get_data_random(self):
        data_mea = []
        zi = random.randint(1,31)
        luna = random.randint(1,12)
        an = random.randint(2021,2023)
        data_mea.append(zi)
        data_mea.append(luna)
        data_mea.append(an)
        return data_mea
    
    def get_string_random(self):
        lg = random.randint(1, 15)
        litere = string.ascii_lowercase
        ret = ''.join(random.choice(litere) for i in range(lg))
        return ret

#------------------------------------------------------------------------Clasa de ServiceStudent----------------------------------------------------------------------------------

class ServiceStudent(object):
    
    
    def __init__(self, valid_student, repo_student):
        self.__valid_student = valid_student
        self.__repo_student = repo_student
    
    def adauga_student(self,id_student,nume,grupa):
        #functie care adauga student in repozitorii
        #input - id_student - int
        #        nume - str
        #        grupa - int
        #rez: adaugarea
        student = Student(id_student,nume,grupa)
        self.__valid_student.valideaza(student)
        self.__repo_student.adauga_student(student)
    
    def get_all_studenti(self):
        #functie care returneaza toti studentii
        return self.__repo_student.get_all()

    def sterge_student(self,id_student):
        #functie care sterge student dupa id
        #input: id_student - int
        self.__valid_student.valideaza_id_(id_student)
        self.__repo_student.sterge_student_dupa_id(id_student)
    
    def modifica_nume_student(self,id_student,nume_nou):
        #functie care modifica numele unui student, dupa id
        #input - id_student - int
        #        nume_nou - str
        self.__valid_student.valideaza_nume_nou(nume_nou)
        self.__repo_student.modifica_nume_student(id_student,nume_nou)
    
    def modifica_grupa_student(self,id_student,grupa_noua):
        #functie care modifica grupa unui student, dupa id
        #input - id_student - int
        #        grupa_noua - int
        self.__valid_student.valideaza_grupa_noua(grupa_noua)
        self.__repo_student.modifica_grupa_student(id_student,grupa_noua)
    
    def cauta_student(self,id_student):
        #funcite care cauta un student dupa id
        #input - id_student - int
        #output: studentul
        self.__valid_student.valideaza_id_(id_student)
        #return self.repo_student.cauta_student_dupa_id(id_student)
        poz = 0
        return self.__repo_student.cauta_student_dupa_id_recursiv(id_student,poz)
    
    
    
    def filtreaza_nume(self,prefix):
        #functie care filtreaza toti studentii care incep cu un prefix
        #input: prefix - str
        #output: lista cu toti studentii
        self.__valid_student.valideaza_prefix(prefix)
        return self.__repo_student.filtreaza_dupa_prefix_nume(prefix)
    
    def get_id_random(self):
        id_meu = random.randint(1, 1000)
        return id_meu
    
    def get_string_random(self):
        lg = random.randint(1, 15)
        litere = string.ascii_lowercase
        ret = ''.join(random.choice(litere) for i in range(lg))
        return ret
    
    def sorteaza_alfabetic(self):
        s = self.__repo_student.get_all()
        studenti = s[:]
        #studenti.sort(key = lambda student : student.get_nume())
        sorter = InsertSort()
        sorter.sort(studenti, key = lambda student : student.get_nume())
        return studenti


#---------------------------------------------------------------------------Clasa de ServiceNote-----------------------------------------------------------------------------------

class ServiceNote(object):
    
    
    def __init__(self, valid_note, repo_note, repo_student, repo_lab):
        self.__valid_note = valid_note
        self.__repo_note = repo_note
        self.__repo_student = repo_student
        self.__repo_lab = repo_lab
    
    def asignare_nota(self,id_nota,id_student,nrlab_nrpb):
        #functie care asigneaza o nota nula la inceput unui student, pt o problema de lab, in repozitorii
        #input - id_nota - int
        #        id_student - int
        #        nrlab_nrpb - str
        #rez: asignarea
        student = self.__repo_student.cauta_student_dupa_id(id_student)
        lab = self.__repo_lab.cauta_lab_dupa_id(nrlab_nrpb)
        nota = Note(id_nota,student,lab)
        self.__valid_note.valideaza_id(nota)
        
        nota_dto = NoteDTO(id_nota,id_student,nrlab_nrpb,0)
        self.__valid_note.valideaza_id(nota_dto)
        self.__repo_note.asignare_nota(nota_dto) 
        
    def adauga_nota(self,id_nota,nota_pb):
        #functie care adauga student in repozitorii
        #input - id_nota - int
        #        nota_pb - float 
        #rez: adaugarea (de fapt, setarea notei)
        nota = self.__repo_note.cauta_nota_dupa_id(id_nota)
        self.__valid_note.valideaza(nota,nota_pb)
        self.__repo_note.adauga_nota(nota,nota_pb)
    
    def sterge_nota(self, id_nota):
        #daca se sterge un student, e musai sa se stearga si notele legate de el
        self.__valid_note.valideaza_id_nota(id_nota)
        self.__repo_note.sterge_nota_dupa_id_nota(id_nota)
    
    def sterge_student_nota(self, id_student):
        #daca se sterge un student, e musai sa se stearga si notele legate de el
        self.__valid_note.valideaza_id_student(id_student)
        self.__repo_note.sterge_nota_dupa_id_student(id_student)
        
    def sterge_lab_nota(self, nrlab_nrpb):
        #daca se sterge un lab, e musai sa se stearga si notele legate de el
        self.__valid_note.valideaza_id_lab(nrlab_nrpb)
        self.__repo_note.sterge_nota_dupa_id_lab(nrlab_nrpb)
    

    def get_all_note(self):
        #functie care returneaza toate notele sub forma unui string
        #output: de_afisat - sir configurat dupa dorinta mea
        notele = self.__repo_note.get_all()
        de_afisat = ""
        id_uri_parcurse = []
        for nota in notele:
            student = self.__repo_student.cauta_student_dupa_id(nota.get_student().get_id_student())
            id_curent = student.get_id_student()
            gasit = 0
            for _id in id_uri_parcurse:
                if _id == id_curent:
                    gasit = 1
            if gasit == 0:
                id_uri_parcurse.append(id_curent)
                de_afisat = de_afisat + "Studentul " + student.get_nume() + " are nota/notele:\n"
                for nota_anumit_student in notele:
                    student_anumit = self.__repo_student.cauta_student_dupa_id(nota_anumit_student.get_student().get_id_student())
                    lab_anumit = self.__repo_lab.cauta_lab_dupa_id(nota_anumit_student.get_lab().get_nrlab_nrpb())
                    id_anumit = student_anumit.get_id_student()
                    if id_curent == id_anumit:
                        de_afisat = de_afisat + "\t" + "-> pentru laboratorul " + lab_anumit.get_nrlab_nrpb() + " : " + str(nota_anumit_student.get_nota_pb()) + " (cu id nota: " + str(nota_anumit_student.get_id_nota()) + ")\n"
        return de_afisat
    
    def get_all_note_sir(self):
        #functie care returneaza toate notele
        return self.__repo_note.get_all()
    
    def get_all_for_files(self):
        note_dtos = self.__repo_note.get_all()
        note_de_afisat = {}
        self.__repo_student.get_all()
        self.__repo_lab.get_all()
        for nota in note_dtos:
            student = self.__repo_student.cauta_student_dupa_id(nota.get_id_student())
            lab = self.__repo_lab.cauta_lab_dupa_id(nota.get_nrlab_nrpb())
            nota.set_student(student)
            nota.set_lab(lab)
            if lab.get_nrlab_nrpb() not in note_de_afisat:
                note_de_afisat[lab.get_nrlab_nrpb()] = []
            note_de_afisat[lab.get_nrlab_nrpb()].append(nota)
        rezultat = []
        for notabene in note_de_afisat:
            nrlab_nrpb = notabene
            lab = self.__repo_lab.cauta_lab_dupa_id(nrlab_nrpb)
            studenti = note_de_afisat[notabene]
            nota_dto = StudenticuNoteDTO(lab.get_nrlab_nrpb(),studenti)
            rezultat.append(nota_dto)
        return rezultat
            
            

    def get_id_random(self):
        #functie care genereaza random id-uri
        id_meu = random.randint(1, 1000)
        return id_meu
    
    def get_id_random_st(self):
        #functie care genereaza random id pt student, din cele deja existente
        stud = self.__repo_student.get_all()
        secv = []
        for i in stud:
            secv.append(i.get_id_student())
        id_mele = random.choice(secv)
        return id_mele 
    
    def get_string_random(self):
        #functie care genereaza random id pt laborator, din cele deja existente
        lab = self.__repo_lab.get_all()
        secv = []
        for ii in lab:
            secv.append(ii.get_nrlab_nrpb())
        id_mele = random.choice(secv)
        return id_mele 
    
    def my_cmp(self,x,y):
        if self.__repo_student.cauta_student_dupa_id(x.get_id_student()).get_nume()!=self.__repo_student.cauta_student_dupa_id(y.get_id_student()).get_nume():
            return self.__repo_student.cauta_student_dupa_id(x.get_id_student()).get_nume()<self.__repo_student.cauta_student_dupa_id(y.get_id_student()).get_nume()
        return x.get_nota()>y.get_nota()
    
    def raport_studenti_cu_note_alfabetic(self, nrlab_nrpb):
        #functie care returneaza note pentru un laborator, ordonate dupa numele studentilor
        #input: nrlab_nrpb - id lab
        self.__valid_note.valideaza_id_lab(nrlab_nrpb)
        self.get_all_for_files()
        all_note = self.__repo_note.get_all_note_dupa_id_problema(nrlab_nrpb)
        note = all_note[:]
        #note.sort(key = lambda nume_student : self.__repo_student.cauta_student_dupa_id(nume_student.get_id_student()).get_nume())
        sorter = InsertSort()
        #sorter.sort(note,key = lambda nume_student : (self.__repo_student.cauta_student_dupa_id(nume_student.get_id_student()).get_nume(), nume_student.get_nota()))
        sorter.sort(note,cmp = lambda x,y: self.my_cmp(x, y))
        for nota in note:
            id_student = nota.get_id_student()
            nota.set_student(self.__repo_student.cauta_student_dupa_id(id_student))
            id_lab = nota.get_nrlab_nrpb()
            nota.set_lab(self.__repo_lab.cauta_lab_dupa_id(id_lab))
        return note
    
    def raport_studenti_cu_note_dupa_nota(self, nrlab_nrpb):
        #functie care returneaza note pentru un laborator, ordonate descrescator dupa nota
        #input: nrlab_nrpb - id lab
        self.__valid_note.valideaza_id_lab(nrlab_nrpb)
        self.get_all_for_files()
        all_note = self.__repo_note.get_all_note_dupa_id_problema(nrlab_nrpb)
        note = all_note[:]
        #note.sort(key = lambda nota : nota.get_nota(), reverse = True)
        sorter = InsertSort()
        sorter.sort(note, key = lambda nota : nota.get_nota(), reverse = True)
        for nota in note:
            id_student = nota.get_id_student()
            nota.set_student(self.__repo_student.cauta_student_dupa_id(id_student))
            id_lab = nota.get_nrlab_nrpb()
            nota.set_lab(self.__repo_lab.cauta_lab_dupa_id(id_lab))
        return note
    
    def raport_studenti_medie_sub_cinci(self):
        #functie care returneaza o lista cu numele studentilor care au media sub cinci, respectiv acea medie
        self.get_all_for_files()
        all_note = self.__repo_note.get_all_pt_raport()
        note = all_note[:]
        id_uri_parcurse = []
        media = []
        for nota in note:
            student = self.__repo_student.cauta_student_dupa_id(nota.get_id_student())
            id_curent = student.get_id_student()
            gasit = 0
            for _id in id_uri_parcurse:
                if _id == id_curent:
                    gasit = 1
            if gasit == 0:
                id_uri_parcurse.append(id_curent)
                cate_note = 0
                media_unui_student = 0
                for nota_anumit_student in note:
                    student_anumit = self.__repo_student.cauta_student_dupa_id(nota_anumit_student.get_id_student())
                    id_anumit = student_anumit.get_id_student()
                    if id_curent == id_anumit:
                        cate_note += 1
                        media_unui_student += nota_anumit_student.get_nota()
                media_unui_student = media_unui_student/cate_note
                if media_unui_student < 5:
                    media.append([student.get_nume(),media_unui_student])
        return media
    
    def raport_primele_cincizeci_la_suta_note_lab_medie_peste_cinci(self):
        #functie care returneaza primele 50% note cu media peste 5
        self.get_all_for_files()
        all_note = self.__repo_note.get_all_pt_raport()
        note = all_note[:]
        note_laborator = {}
        medie_lab = {}
        for nota in note:
            id_lab = nota.get_nrlab_nrpb()
            #parti = id_lab.split("_")
            #nrlab = int(parti[0])
            if id_lab not in note_laborator:
                note_laborator[id_lab] = 0
            note_laborator[id_lab] += 1
            if id_lab not in medie_lab:
                medie_lab[id_lab] = 0
            medie_lab[id_lab] += nota.get_nota()
        rezultat = []
        for cheie in note_laborator:
            id_laborator = cheie
            nr_note = note_laborator[cheie]
            medie_note_lab = medie_lab[cheie]/nr_note
            if medie_note_lab >= 5:
                nota_DTO = Note_DTO(id_laborator, medie_note_lab, nr_note)
                rezultat.append(nota_DTO)
        #rezultat.sort(key = lambda id_lab: id_lab.get_id_lab())
        sorter = CombSort()
        sorter.sort(rezultat, key = lambda id_lab: id_lab.get_id_lab())
        return rezultat[:len(rezultat)//2]

