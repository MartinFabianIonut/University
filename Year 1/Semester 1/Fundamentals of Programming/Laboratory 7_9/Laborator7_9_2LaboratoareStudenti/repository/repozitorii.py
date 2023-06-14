from erors.exceptii import RepositoryError
from domain.entitati import Laborator, Student
from domain.dtos import NoteDTO

#--------------------------------------------------------------------------Clasa de RepoLaborator-----------------------------------------------------------------------------------

class RepoLaborator(object):
    
    
    def __init__(self):
        self._lab = []
    
    def __len__(self):
        return len(self._lab)

    def adauga_lab(self,lab): 
        #funcite care adauga laboratorul
        #input: lab - obiect
        for _lab in self._lab:
            if _lab == lab:
                raise RepositoryError("Id lab existent!\n")
        self._lab.append(lab)
        
    def cauta_lab_dupa_id(self, nrlab_nrpb):
        #functie care cauta laborator dupa id
        #input: nrlab_nrpb - str
        #rezultat: returneaza lab, daca id-ul exista
        #          eroare de repo, altfel
        ok = True
        for _lab in self._lab:
            if _lab.get_nrlab_nrpb() == nrlab_nrpb:
                return _lab
        if ok:
            raise RepositoryError("Id lab inexistent!\n")
        
    def cauta_lab_dupa_id_recursiv(self, nrlab_nrpb, poz):
        #functie recursiva care cauta laboratorul dupa id
        #input: nrlab_nrpb - str
        #       poz - pozitia din sirul de studenti
        #output: laboratorul, daca exista
        #        eroare de repo - in caz contrar
        if len(self._lab) == poz:
            raise RepositoryError("Id lab inexistent!\n")
        lab_curent = self._lab[poz]
        if lab_curent.get_nrlab_nrpb() == nrlab_nrpb:
            return lab_curent
        return self.cauta_lab_dupa_id_recursiv(nrlab_nrpb, poz+1)
    
    def sterge_lab_dupa_id(self, nrlab_nrpb):
        #functie care sterge laborator dupa id
        #input: nrlab_nrpb - str
        #rezultat: face stergerea, daca id-ul exista
        #          eroare de repo, altfel
        ok = True
        for _lab in self._lab:
            if _lab.get_nrlab_nrpb() == nrlab_nrpb:
                self._lab.remove(_lab)
                ok = False
        if ok:
            raise RepositoryError("Id lab inexistent!\n")
        
    def modifica_descriere_lab(self,nrlab_nrpb,descriere_noua):
        #functie care modifica descrierea laboratorului dupa id
        #input: nrlab_nrpb - str
        #       descriere_noua - str
        ok = True
        for _lab in self._lab:
            if _lab.get_nrlab_nrpb() == nrlab_nrpb:
                _lab.set_descriere(descriere_noua)
                ok = False
        if ok:
            raise RepositoryError("Id lab inexistent!\n")
        
    def modifica_deadline_lab(self,nrlab_nrpb,deadline_nou):
        #functie care modifica descrierea laboratorului dupa id
        #input: nrlab_nrpb - str
        #       deadline_nou - lista cu zi, luna, an
        ok = True
        for _lab in self._lab:
            if _lab.get_nrlab_nrpb() == nrlab_nrpb:
                _lab.set_deadline(deadline_nou)
                ok = False
        if ok:
            raise RepositoryError("Id lab inexistent!\n")
        
    def get_all(self):
        #functie care returneaza toate laboratoarele
        return self._lab

class FileRepoLaborator(RepoLaborator):
    
    def __init__(self, file_path):
        RepoLaborator.__init__(self)
        self.__file_path = file_path
        
    def __len__(self):
        self.__read_all_from_file()
        return RepoLaborator.__len__(self)
        
    def __read_all_from_file(self):
        self._lab = []
        '''
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip()
                parti = linie.split(",")
                if len(parti)>0:
                    nrlab_nrpb = parti[0]
                    descriere = parti[1]
                    deadline = []
                    parti_deadline = parti[2].split(".")
                    deadline.append(int(parti_deadline[0]))
                    deadline.append(int(parti_deadline[1]))
                    deadline.append(int(parti_deadline[2]))
                    lab = Laborator(nrlab_nrpb,descriere,deadline)
                    self._lab.append(lab)
        '''
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            i = 0
            while i+3<=len(linii):
                linii[i] = linii[i].strip()
                linii[i+1] = linii[i+1].strip()
                linii[i+2] = linii[i+2].strip()
                if len(linii[i])>0 and len(linii[i+1])>0 and len(linii[i+2])>0:
                    nrlab_nrpb = linii[i]
                    descriere = linii[i+1]
                    deadline = []
                    parti_deadline = linii[i+2].split(".")
                    deadline.append(int(parti_deadline[0]))
                    deadline.append(int(parti_deadline[1]))
                    deadline.append(int(parti_deadline[2]))
                    lab = Laborator(nrlab_nrpb,descriere,deadline)
                    self._lab.append(lab)
                i+=3
        
    
    def __append_to_file(self, lab):
        with open(self.__file_path,"a") as f:
            deadline_curent = lab.get_deadline()
            #f.write(lab.get_nrlab_nrpb()+","+lab.get_descriere()+","+str(deadline_curent[0]) + "." + str(deadline_curent[1]) + "." + str(deadline_curent[2]) + "\n")
            f.write(lab.get_nrlab_nrpb()+"\n"+lab.get_descriere()+"\n"+str(deadline_curent[0]) + "." + str(deadline_curent[1]) + "." + str(deadline_curent[2]) + "\n")
    
    def adauga_lab(self, lab):
        self.__read_all_from_file()
        RepoLaborator.adauga_lab(self, lab)
        self.__append_to_file(lab)

    def __write_all_to_file(self):
        with open(self.__file_path,"w") as f:
            for lab in self._lab:
                deadline_curent = lab.get_deadline()
                #f.write(lab.get_nrlab_nrpb()+","+lab.get_descriere()+","+str(deadline_curent[0]) + "." + str(deadline_curent[1]) + "." + str(deadline_curent[2]) + "\n")
                f.write(lab.get_nrlab_nrpb()+"\n"+lab.get_descriere()+"\n"+str(deadline_curent[0]) + "." + str(deadline_curent[1]) + "." + str(deadline_curent[2]) + "\n")
    
    def sterge_lab_dupa_id(self, nrlab_nrpb):
        self.__read_all_from_file()
        RepoLaborator.sterge_lab_dupa_id(self, nrlab_nrpb)
        self.__write_all_to_file()
        
    def cauta_lab_dupa_id(self, nrlab_nrpb):
        self.__read_all_from_file()
        return RepoLaborator.cauta_lab_dupa_id(self, nrlab_nrpb)
    
    def modifica_descriere_lab(self, nrlab_nrpb, descriere_noua):
        self.__read_all_from_file()
        RepoLaborator.modifica_descriere_lab(self, nrlab_nrpb, descriere_noua)
        self.__write_all_to_file()
        
    def modifica_deadline_lab(self, nrlab_nrpb, deadline_nou):
        self.__read_all_from_file()
        RepoLaborator.modifica_deadline_lab(self, nrlab_nrpb, deadline_nou)
        self.__write_all_to_file()
    
    def get_all(self):
        self.__read_all_from_file()
        return RepoLaborator.get_all(self)

#--------------------------------------------------------------------------Clasa de RepoStudent-----------------------------------------------------------------------------------

class RepoStudent(object):
    
    
    def __init__(self):
        self._student = []
    
    def __len__(self):
        return len(self._student)

    def adauga_student(self,student):
        #funcite care adauga studentul
        #input: student - obiect
        for _student in self._student:
            if _student == student:
                raise RepositoryError("Id student existent!\n")
        self._student.append(student) 
        
    def cauta_student_dupa_id(self, id_student):
        #functie care cauta student dupa id
        #input: id_student - int
        #output: student, daca id-ul exista
        #        eroare de repo, altfel
        ok = True
        for _student in self._student:
            if _student.get_id_student() == id_student:
                return _student
        if ok:
            raise RepositoryError("Id student inexistent!\n")
        
    def cauta_student_dupa_id_recursiv(self, id_student, poz):
        #functie recursiva care cauta studentul dupa id
        #input: id_student - int
        #       poz - pozitia din sirul de studenti
        #output: student, daca exista
        #        eroare de repo - in caz contrar
        if len(self._student) == poz:
            raise RepositoryError("Id student inexistent!\n")
        student_curent = self._student[poz]
        if student_curent.get_id_student() == id_student:
            return student_curent
        return self.cauta_student_dupa_id_recursiv(id_student, poz+1)
    
    def sterge_student_dupa_id(self, id_student):
        #functie care sterge student dupa id
        #input: id_student - int
        #output: sterge studentul, daca id-ul exista
        #        eroare de repo, altfel
        ok = True
        for _student in self._student:
            if _student.get_id_student() == id_student:
                self._student.remove(_student)
                ok = False
        if ok:
            raise RepositoryError("Id student inexistent!\n")
        
    def modifica_nume_student(self,id_student,nume_nou):
        #functie care modifica nume student dupa id
        #input: id_student - int
        #       nume_nou - str
        ok = True
        for _student in self._student:
            if _student.get_id_student() == id_student:
                _student.set_nume(nume_nou)
                ok = False
        if ok:
            raise RepositoryError("Id student inexistent!\n")
        
    def modifica_grupa_student(self,id_student,grupa_noua):
        #functie care modifica grupa student dupa id
        #input: id_student - int
        #       grupa_noua - int
        ok = True
        for _student in self._student:
            if _student.get_id_student() == id_student:
                _student.set_grupa(grupa_noua)
                ok = False
        if ok:
            raise RepositoryError("Id student inexistent!\n")
        
    def get_all(self):
        #functie care returneaza tot studentii
        return self._student
    
    def filtreaza_dupa_prefix_nume(self, prefix):
        #functie care filtreaza dupa un prefix numele studentilor
        #input: prfix - str
        #output: sir_studenti - lista cu studentii filtrati
        ok = True
        lungime_prefix = len(prefix)
        sir_studenti = []
        for _student in self._student:
            nume = _student.get_nume()
            if nume.find(prefix,0,lungime_prefix)!=-1:
                sir_studenti.append(_student)
                ok = False
        if ok:
            raise RepositoryError("Nu exista studenti care sa aiba prefix sirul citit!\n")
        return sir_studenti
            

class FileRepoStudent(RepoStudent):
    
    def __init__(self, file_path):
        RepoStudent.__init__(self)
        self.__file_path = file_path
        
    def __len__(self):
        self.__read_all_from_file()
        return RepoStudent.__len__(self)
    
    def __read_all_from_file(self):
        self._student = []
        '''
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip()
                parti = linie.split(",")
                if len(parti)>0:
                    id_student = int(parti[0])
                    nume = parti[1]
                    grupa = int(parti[2])
                    student = Student(id_student,nume,grupa)
                    self._student.append(student)
        '''
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            i = 0
            while i+3<=len(linii):
                linii[i] = linii[i].strip()
                linii[i+1] = linii[i+1].strip()
                linii[i+2] = linii[i+2].strip()
                if len(linii[i])>0 and len(linii[i+1])>0 and len(linii[i+2])>0:
                    id_student = int(linii[i])
                    nume = linii[i+1]
                    grupa = int(linii[i+2])
                    student = Student(id_student,nume,grupa)
                    self._student.append(student)
                i+=3
        
    
    def __append_to_file(self, student):
        with open(self.__file_path,"a") as f:
            #f.write(str(student.get_id_student()) + "," + student.get_nume() + "," + str(student.get_grupa()) + "\n")
            f.write(str(student.get_id_student()) + "\n" + student.get_nume() + "\n" + str(student.get_grupa()) + "\n")
    
    def adauga_student(self, student):
        self.__read_all_from_file()
        RepoStudent.adauga_student(self, student)
        self.__append_to_file(student)
        

    def __write_all_to_file(self):
        with open(self.__file_path,"w") as f:
            for student in self._student:
                #f.write(str(student.get_id_student()) + "," + student.get_nume() + "," + str(student.get_grupa()) + "\n")
                f.write(str(student.get_id_student()) + "\n" + student.get_nume() + "\n" + str(student.get_grupa()) + "\n")
    
    def sterge_student_dupa_id(self, id_student):
        self.__read_all_from_file()
        RepoStudent.sterge_student_dupa_id(self, id_student)
        self.__write_all_to_file()
        
    def cauta_student_dupa_id_recursiv(self, id_student, poz):
        self.__read_all_from_file()
        return RepoStudent.cauta_student_dupa_id_recursiv(self, id_student, poz)
    
    def get_all(self):
        self.__read_all_from_file()
        return RepoStudent.get_all(self)
    
    def modifica_nume_student(self, id_student, nume_nou):
        self.__read_all_from_file()
        RepoStudent.modifica_nume_student(self, id_student, nume_nou)
        self.__write_all_to_file()
        
    def modifica_grupa_student(self, id_student, grupa_noua):
        self.__read_all_from_file()
        RepoStudent.modifica_grupa_student(self, id_student, grupa_noua)
        self.__write_all_to_file()
        
#--------------------------------------------------------------------------Clasa de RepoNote--------------------------------------------------------------------------------------

class RepoNote(object):
    
    def __init__(self):
        self._note = []
    
    def __len__(self):
        return len(self._note)
    
    def asignare_nota(self,nota):
        #functie care asigneaza o nota pentru un student si o problema de laborator
        #input: nota - obiect care contine id,referinta la student, referinta la lab si nota
        #rezultat: asigneaza nota daca:
        #                             - id-ul notei este unic
        #                             - un student nu poate avea asignata de mai multe ori aceeasi problema
        #          eroare de repo, in caz contrar (adica problema a fost deja asignata)
        for _nota in self._note:
            if _nota == nota or _nota.get_id_student() == nota.get_id_student() and _nota.get_nrlab_nrpb() == nota.get_nrlab_nrpb():
                raise RepositoryError("Problema de laborator deja asignata!\n")
        self._note.append(nota)

    def adauga_nota(self,nota,nota_pb):
        #functie care adauga o nota pentru o nota deja asignata (adica o seteaza)
        #input: nota - obiect
        #       nota_pb - float (1<= nota_pb <=10)
        for _nota in self._note:
            if _nota == nota:
                _nota.set_nota_lab(nota_pb)
                
    def sterge_nota_dupa_id_nota(self, id_nota):
        #functie care sterge o nota dupa id
        #input: id_nota - int
        #output: se va sterge nota, daca id-ul exista
        #        eroare de repo, in caz ca acel id nu exista
        ok = True
        lg = len(self._note)
        i = 1
        while i <= lg:
            _nota = self._note[i-1]
            if _nota.get_id_nota() == id_nota:
                self._note.remove(_nota)
                ok = False
                lg -= 1
            else:
                i += 1
        if ok:
            raise RepositoryError("Id nota laborator inexistent!\n")
                
    def sterge_nota_dupa_id_student(self, id_student):
        #functie care sterge toate notele de la un id de student
        #input: id_student - int
        #output: se vor sterge toate aparitiile de note pt id-ul introdus, daca exista
        #        eroare de repo, in caz ca acel id nu exista
        ok = True
        lg = len(self._note)
        i = 1
        while i <= lg:
            _nota = self._note[i-1]
            if _nota.get_id_student() == id_student:
                self._note.remove(_nota)
                ok = False
                lg -= 1
            else:
                i += 1
        if ok:
            raise RepositoryError("Id student inexistent!\n")
                
        
    
    def sterge_nota_dupa_id_lab(self, nrlab_nrpb):
        #functie care sterge toate notele de la un id de laborator
        #input: nrlab_nrpb - str
        #output: se vor sterge toate aparitiile de note pt id-ul introdus, daca exista
        #        eroare de repo, in caz ca acel id nu exista
        ok = True
        lg = len(self._note)
        i = 1
        while i <= lg:
            _nota = self._note[i-1]
            if _nota.get_nrlab_nrpb() == nrlab_nrpb:
                self._note.remove(_nota)
                ok = False
                lg -= 1
            else:
                i += 1
        if ok:
            raise RepositoryError("Id lab inexistent!\n")
        
    def cauta_nota_dupa_id(self, id_nota):
        #functie care cauta nota dupa id
        #input: id_nota - int
        #output: nota, daca exista
        #        eroare de repo - in caz contrar
        ok = True
        for _nota in self._note:
            if _nota.get_id_nota()== id_nota:
                return _nota
        if ok:
            raise RepositoryError("Id nota laborator inexistent!\n")
        
    
        
    def get_all(self):
        #functie care returneaza toate notele
        return self._note
    
    
    def get_all_note_dupa_id_problema(self, nrlab_nrpb):
        #functie care returneaza toate notele de la un anumit laborator
        #input: nrlab_nrpb - str (identificatorul laboratorului)
        #output: note_anumita_problema - lista cu notele corespunzatoare
        note_anumita_problema = []
        cate_note = 0
        for _nota in self._note:
            #print(_nota.get_nrlab_nrpb())
            if _nota.get_nrlab_nrpb() == nrlab_nrpb:
                cate_note += 1
                #print(str(_nota.get_nota()))
                if _nota.get_nota()!=0:
                    note_anumita_problema.append(_nota)
        if note_anumita_problema == []:
            raise RepositoryError("Aceasta problema nu a fost asignata niciunui student SAU a fost asignata si nu notata!\n")
        if len(note_anumita_problema) != cate_note:
            raise RepositoryError("Exista laborator/oare asignat/e si nenotat/e!\n")
        return note_anumita_problema
    
    def get_all_pt_raport(self):
        #functie care returneaza toate notele, doar daca toate notele au fost nu doar asignate, ci si notate
        for nota in self._note:
            if nota.get_nota() == 0:
                raise RepositoryError("Exista laborator asignat si nenotat!\n")
        return self._note
    
class FileRepoNote(RepoNote):
    
    def __init__(self,file_path):
        RepoNote.__init__(self)
        self.__file_path = file_path
    
    def __read_all_from_file(self):
        self._note = []
        '''
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            for linie in linii:
                linie = linie.strip()
                if len(linie)>0:
                    parti = linie.split(",")
                    id_nota = int(parti[0])
                    id_student = int(parti[1])
                    nrlab_nrpb = parti[2]
                    nota_pb = float(parti[3])
                    nota = NoteDTO(id_nota,id_student,nrlab_nrpb,nota_pb)
                    self._note.append(nota)
        '''
        with open(self.__file_path,"r") as f:
            linii = f.readlines()
            i = 0
            while i+4<=len(linii):
                linii[i] = linii[i].strip()
                linii[i+1] = linii[i+1].strip()
                linii[i+2] = linii[i+2].strip()
                linii[i+3] = linii[i+3].strip()
                if len(linii[i])>0 and len(linii[i+1])>0 and len(linii[i+2])>0 and len(linii[i+3])>0:
                    id_nota = int(linii[i])
                    id_student = int(linii[i+1])
                    nrlab_nrpb = linii[i+2]
                    nota_pb = float(linii[i+3])
                    nota = NoteDTO(id_nota,id_student,nrlab_nrpb,nota_pb)
                    self._note.append(nota)
                i+=4
        
    
    def get_all(self):
        self.__read_all_from_file()
        return RepoNote.get_all(self)
    

    def __append_to_file(self,nota):
        with open(self.__file_path,"a") as f:
            #f.write(str(nota.get_id_nota())+","+str(nota.get_id_student())+","+str(nota.get_nrlab_nrpb()) + "," + str(nota.get_nota()) + "\n")
            f.write(str(nota.get_id_nota())+"\n"+str(nota.get_id_student())+"\n"+str(nota.get_nrlab_nrpb()) + "\n" + str(nota.get_nota()) + "\n")
    
    def asignare_nota(self, nota):
        self.__read_all_from_file()
        RepoNote.asignare_nota(self, nota)
        self.__append_to_file(nota)
        

    def __write_all_to_file(self):
        with open(self.__file_path,"w") as f:
            for nota in self._note:
                #f.write(str(nota.get_id_nota())+","+str(nota.get_id_student())+","+str(nota.get_nrlab_nrpb()) + "," + str(nota.get_nota()) + "\n")
                f.write(str(nota.get_id_nota())+"\n"+str(nota.get_id_student())+"\n"+str(nota.get_nrlab_nrpb()) + "\n" + str(nota.get_nota()) + "\n")
    
    def adauga_nota(self, nota, nota_pb):
        self.__read_all_from_file()
        RepoNote.adauga_nota(self, nota, nota_pb)
        self.__write_all_to_file()
        
    def sterge_nota_dupa_id_student(self, id_student):
        self.__read_all_from_file()
        RepoNote.sterge_nota_dupa_id_student(self, id_student)
        self.__write_all_to_file()
    
    def sterge_nota_dupa_id_lab(self, nrlab_nrpb):
        self.__read_all_from_file()
        RepoNote.sterge_nota_dupa_id_lab(self, nrlab_nrpb)
        self.__write_all_to_file()
        
    def sterge_nota_dupa_id_nota(self, id_nota):
        self.__read_all_from_file()
        RepoNote.sterge_nota_dupa_id_nota(self, id_nota)
        self.__write_all_to_file()
