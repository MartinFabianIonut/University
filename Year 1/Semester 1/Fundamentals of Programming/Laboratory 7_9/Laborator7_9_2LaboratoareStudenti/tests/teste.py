from domain.entitati import Student, Laborator,Note
from validation.validatori import ValidatorStudent, ValidatorLaborator,ValidatorNote
from erors.exceptii import ValidationError, RepositoryError
from repository.repozitorii import RepoStudent, RepoLaborator, RepoNote,\
    FileRepoStudent, FileRepoLaborator, FileRepoNote
from controller.servicii import ServiceStudent, ServiceLaborator, ServiceNote
from domain.dtos import NoteDTO
from sorters.sortari import InsertSort, CombSort, MergeSort

class Teste(object):
    
    
    def __test_creeaza_student_succes(self, id_student, nume, grupa):
        student = Student(id_student,nume,grupa)
        assert (student.get_id_student()==id_student)
        assert(student.get_nume()==nume)
        assert(abs(student.get_grupa()-grupa)<0.0001)
        student.set_nume("Mircea Vasile")
        student.set_grupa(211)
        assert (student.get_id_student()==id_student)
        assert(student.get_nume()=="Mircea Vasile")
        assert(abs(student.get_grupa()-211)<0.0001)
        return student
    
    
    def __test_egalitate_studenti(self, student, id_student, alt_nume, alta_grupa):
        alt_student_acelasi_id = Student(id_student,alt_nume,alta_grupa)
        assert(alt_student_acelasi_id == student)
    
    
    def __test_printeaza_student(self, student):
        assert (str(student)=="Id: 1023 -> Nume: Mircea Vasile, din grupa 211.")
    
    
    def __run_creeaza_student_test(self):
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        student = self.__test_creeaza_student_succes(id_student, nume, grupa)
        alt_nume = "Andrei Georgescu"
        alta_grupa = 212
        self.__test_egalitate_studenti(student,id_student,alt_nume,alta_grupa)
        self.__test_printeaza_student(student)
    
    
    def __test_valideaza_student_succes(self, id_student, nume, grupa):
        student = Student(id_student,nume,grupa)
        valid_student = ValidatorStudent()
        valid_student.valideaza(student)
        assert (True)
        return valid_student
    
    
    def __test_valideaza_student_id_invalid(self, valid_student, inv_id_student, nume, grupa):
        student = Student(inv_id_student,nume,grupa)
        try:
            valid_student.valideaza(student)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Id invalid!\n")
    
    
    def __test_valideaza_student_nume_invalid(self, valid_student, id_student, inv_nume, grupa):
        student = Student(id_student,inv_nume,grupa)
        try:
            valid_student.valideaza(student)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Nume invalid!\n")
    
    
    def __test_valideaza_student_grupa_invalida(self, valid_student, id_student, nume, inv_grupa):
        student = Student(id_student,nume,inv_grupa)
        try:
            valid_student.valideaza(student)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Grupa invalida!\n")
    
    
    def __test_valideaza_student_toate_invalide(self, valid_student, inv_id_student, inv_nume, inv_grupa):
        student = Student(inv_id_student,inv_nume,inv_grupa)
        try:
            valid_student.valideaza(student)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Id invalid!\nNume invalid!\nGrupa invalida!\n")
    
    
    def __run_valideaza_student_tests(self):
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        valid_student = self.__test_valideaza_student_succes(id_student,nume,grupa)
        inv_id_student = -23
        inv_nume = ""
        inv_grupa = -2
        self.__test_valideaza_student_id_invalid(valid_student,inv_id_student,nume,grupa)
        self.__test_valideaza_student_nume_invalid(valid_student,id_student,inv_nume,grupa)
        self.__test_valideaza_student_grupa_invalida(valid_student,id_student,nume,inv_grupa)
        self.__test_valideaza_student_toate_invalide(valid_student,inv_id_student,inv_nume,inv_grupa)
    
    
    def __test_creeaza_repo_vid(self):
        file_path = "tests/test_studenti.txt"
        with open(file_path,"w") as f:
            f.write("")
        repo = FileRepoStudent(file_path)
        assert(len(repo)==0)
        return repo
    
    
    def __test_adauga_student_repo_succes(self, repo, student):
        repo.adauga_student(student)
        assert(len(repo)==1)
    
    
    def __test_cauta_student_id_inexistent(self, repo, inex_id_student):
        try:
            repo.cauta_student_dupa_id(inex_id_student)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __test_adauga_student_acelasi_id(self, repo, alt_student_acelasi_id):
        try:
            repo.adauga_student(alt_student_acelasi_id)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id student existent!\n")
    
    
    def __run_repo_adauga_student_tests(self):
        repo = self.__test_creeaza_repo_vid()
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        student = Student(id_student,nume,grupa)
        self.__test_adauga_student_repo_succes(repo,student)
        student_gasit = repo.cauta_student_dupa_id(id_student)
        assert(student.get_nume()==student_gasit.get_nume())
        assert(abs(student.get_grupa()-student_gasit.get_grupa())<0.0001)
        inex_id_student = 142
        alt_nume = "Vlad Marin"
        alta_grupa = 221
        self.__test_cauta_student_id_inexistent(repo,inex_id_student)
        alt_student_acelasi_id = Student(id_student,alt_nume,alta_grupa)
        self.__test_adauga_student_acelasi_id(repo,alt_student_acelasi_id)
    
    
    def __test_adauga_student_srv_succes(self, srv, id_student, nume, grupa):
        all_el = srv.get_all_studenti()
        assert(len(all_el)==0)
        srv.adauga_student(id_student,nume,grupa)
        all_el = srv.get_all_studenti()
        assert(len(all_el)==1)
        assert(all_el[0].get_id_student()==id_student)
        assert(all_el[0].get_nume()==nume)
        assert(abs(all_el[0].get_grupa()-grupa)<0.0001)
    
    
    def __test_adauga_student_srv_id_invalid(self, srv, inv_id_student, nume, grupa):
        try:
            srv.adauga_student(inv_id_student,nume,grupa)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id invalid!\n")
    
    
    def __test_adauga_student_srv_nume_invalid(self, srv, id_student, inv_nume, grupa):
        try:
            srv.adauga_student(id_student,inv_nume,grupa)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Nume invalid!\n")
    
    
    def __test_adauga_student_srv_grupa_invalida(self, srv, id_student, nume, inv_grupa):
        try:
            srv.adauga_student(id_student,nume,inv_grupa)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Grupa invalida!\n")
    
    def __test_adauga_student_srv_toate_invalide(self, srv, inv_id_student, inv_nume, inv_grupa):
        try:
            srv.adauga_student(inv_id_student,inv_nume,inv_grupa)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id invalid!\nNume invalid!\nGrupa invalida!\n")
    
    
    def __run_srv_adauga_student_tests(self):
        repo = self.__test_creeaza_repo_vid()
        valid_student = ValidatorStudent()
        srv = ServiceStudent(valid_student,repo)
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        inv_id_student = -23
        inv_nume = ""
        inv_grupa = -2
        self.__test_adauga_student_srv_succes(srv,id_student,nume,grupa)
        self.__test_adauga_student_srv_id_invalid(srv,inv_id_student,nume,grupa)
        self.__test_adauga_student_srv_nume_invalid(srv,id_student,inv_nume,grupa)
        self.__test_adauga_student_srv_grupa_invalida(srv,id_student,nume,inv_grupa)
        self.__test_adauga_student_srv_toate_invalide(srv,inv_id_student,inv_nume,inv_grupa)
    

    def __test_sterge_student_repo_succes(self, repo, student):
        repo.adauga_student(student)
        assert(len(repo)==1)
        id_student = student.get_id_student()
        repo.sterge_student_dupa_id(id_student)
        assert(len(repo)==0)
    
    
    def __test_sterge_student_repo_id_inexistent(self, repo, student, inex_id_student):
        repo.adauga_student(student)
        assert(len(repo)==1)
        try:
            repo.sterge_student_dupa_id(inex_id_student)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __run_repo_sterge_student_tests(self):
        repo = self.__test_creeaza_repo_vid()
        student = Student(12,"Gabi",211)
        self.__test_sterge_student_repo_succes(repo,student)
        inex_id_student = 100
        self.__test_sterge_student_repo_id_inexistent(repo, student, inex_id_student)
    
    
    def __test_sterge_student_srv_succes(self, srv, id_student, nume, grupa):
        all_el = srv.get_all_studenti()
        assert(len(all_el)==0)
        srv.adauga_student(id_student,nume,grupa)
        all_el = srv.get_all_studenti()
        assert(len(all_el)==1)
        assert(all_el[0].get_id_student()==id_student)
        assert(all_el[0].get_nume()==nume)
        assert(abs(all_el[0].get_grupa()-grupa)<0.0001)
        srv.sterge_student(id_student)
        all_el = srv.get_all_studenti()
        assert(len(all_el)==0)
    
    
    def __test_sterge_student_srv_id_inexistent(self, srv, id_student, inex_id_student, nume, grupa):
        srv.adauga_student(id_student,nume,grupa)
        try:
            srv.sterge_student(inex_id_student)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __run_srv_sterge_student_tests(self):
        repo = RepoStudent()
        valid_student = ValidatorStudent()
        srv = ServiceStudent(valid_student,repo)
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        inex_id_student = 222
        self.__test_sterge_student_srv_succes(srv,id_student,nume,grupa)
        self.__test_sterge_student_srv_id_inexistent(srv,id_student,inex_id_student,nume,grupa)
    
    
    def __test_modifica_nume_student_repo_succes(self, repo, student,nume_nou):
        repo.adauga_student(student)
        assert(len(repo)==1)
        id_student = student.get_id_student()
        repo.modifica_nume_student(id_student,nume_nou)
        student = repo.cauta_student_dupa_id(id_student)
        assert(student.get_nume()==nume_nou)
    
    
    def __test_modifica_nume_student_repo_id_inexistent(self, repo, nume_nou, inex_id_student):
        assert(len(repo)==1)
        try:
            repo.modifica_nume_student(inex_id_student,nume_nou)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __run_repo_modifica_nume_student_tests(self):
        repo = self.__test_creeaza_repo_vid()
        student = Student(12,"Gabi",211)
        nume_nou = "Denis"
        self.__test_modifica_nume_student_repo_succes(repo,student,nume_nou)
        inex_id_student = 100
        self.__test_modifica_nume_student_repo_id_inexistent(repo, nume_nou, inex_id_student)
    
    
    def __test_modifica_nume_student_srv_succes(self, srv, id_student, nume, grupa, nume_nou):
        srv.adauga_student(id_student,nume,grupa)
        all_el = srv.get_all_studenti()
        assert(all_el[0].get_nume()==nume)
        srv.modifica_nume_student(id_student,nume_nou)
        all_el = srv.get_all_studenti()
        assert(all_el[0].get_nume()==nume_nou)
    
    
    def __test_modifica_nume_student_srv_nume_invalid(self, srv, id_student, inv_nume):
        try:
            srv.modifica_nume_student(id_student,inv_nume)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Nume invalid!\n")
    
    
    def __test_modifica_nume_student_srv_id_inexistent(self, srv, inex_id_student, nume_nou):
        try:
            srv.modifica_nume_student(inex_id_student,nume_nou)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __run_srv_modifica_nume_student_tests(self):
        repo = RepoStudent()
        valid_student = ValidatorStudent()
        srv = ServiceStudent(valid_student,repo)
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        inex_id_student = 222
        nume_nou = "Florin Baias"
        inv_nume = ""
        self.__test_modifica_nume_student_srv_succes(srv,id_student,nume,grupa,nume_nou)
        self.__test_modifica_nume_student_srv_nume_invalid(srv,id_student,inv_nume)
        self.__test_modifica_nume_student_srv_id_inexistent(srv,inex_id_student,nume_nou)
        
        
    def __test_modifica_grupa_student_repo_succes(self, repo, student,grupa_noua):
        repo.adauga_student(student)
        assert(len(repo)==1)
        id_student = student.get_id_student()
        repo.modifica_grupa_student(id_student,grupa_noua)
        student = repo.cauta_student_dupa_id(id_student)
        assert(student.get_grupa()==grupa_noua)
    
    
    def __test_modifica_grupa_student_repo_id_inexistent(self, repo, grupa_noua, inex_id_student):
        assert(len(repo)==1)
        try:
            repo.modifica_grupa_student(inex_id_student,grupa_noua)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __run_repo_modifica_grupa_student_tests(self):
        repo = self.__test_creeaza_repo_vid()
        student = Student(12,"Gabi",211)
        grupa_noua = 192
        self.__test_modifica_grupa_student_repo_succes(repo,student,grupa_noua)
        inex_id_student = 100
        self.__test_modifica_grupa_student_repo_id_inexistent(repo, grupa_noua, inex_id_student)
    
    
    def __test_modifica_grupa_student_srv_succes(self, srv, id_student, nume, grupa, grupa_noua):
        srv.adauga_student(id_student,nume,grupa)
        all_el = srv.get_all_studenti()
        assert(all_el[0].get_nume()==nume)
        srv.modifica_grupa_student(id_student,grupa_noua)
        all_el = srv.get_all_studenti()
        assert(all_el[0].get_grupa()==grupa_noua)
    
    
    def __test_modifica_grupa_student_srv_nume_invalid(self, srv, id_student, inv_grupa):
        try:
            srv.modifica_grupa_student(id_student,inv_grupa)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Grupa invalida!\n")
    
    
    def __test_modifica_grupa_student_srv_id_inexistent(self, srv, inex_id_student, grupa_noua):
        try:
            srv.modifica_grupa_student(inex_id_student,grupa_noua)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __run_srv_modifica_grupa_student_tests(self):
        repo = RepoStudent()
        valid_student = ValidatorStudent()
        srv = ServiceStudent(valid_student,repo)
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        inex_id_student = 222
        grupa_noua = 200
        inv_grupa = -13
        self.__test_modifica_grupa_student_srv_succes(srv,id_student,nume,grupa,grupa_noua)
        self.__test_modifica_grupa_student_srv_nume_invalid(srv,id_student,inv_grupa)
        self.__test_modifica_grupa_student_srv_id_inexistent(srv,inex_id_student,grupa_noua)
    
    
    def __test_cauta_student_repo_succes(self, repo, student, id_student_cautat):
        repo.adauga_student(student)
        assert(len(repo)==1)
        st = repo.cauta_student_dupa_id_recursiv(id_student_cautat,0)
        assert(st.get_id_student()==student.get_id_student())
        assert(st.get_nume()==student.get_nume())
        assert(abs(st.get_grupa()-student.get_grupa())<0.0001)
    
    def __test_cauta_student_repo_id_inexistent(self, repo, inex_id_student):
        assert(len(repo)==1)
        try:
            repo.cauta_student_dupa_id(inex_id_student)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
            
    def __run_repo_cauta_student_tests(self):
        repo = self.__test_creeaza_repo_vid()
        student = Student(12,"Gabi",211)
        id_student_cautat = 12
        self.__test_cauta_student_repo_succes(repo,student,id_student_cautat)
        inex_id_student = 100
        self.__test_cauta_student_repo_id_inexistent(repo, inex_id_student)
    
    
    def __test_cauta_student_srv_succes(self, srv, id_student, nume, grupa, id_student_cautat):
        srv.adauga_student(id_student,nume,grupa)
        gasit = srv.cauta_student(id_student_cautat)
        assert(gasit.get_id_student()==id_student)
        assert(gasit.get_nume()==nume)
        assert(abs(gasit.get_grupa()-grupa)<0.0001)
    
    
    def __test_cauta_student_srv_id_cautat_invalid(self, srv, id_cautat_invalid):
        try:
            srv.cauta_student(id_cautat_invalid)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id invalid!\n")
    
    
    def __test_cauta_student_srv_id_inexistent(self, srv, inex_id_student):
        try:
            srv.cauta_student(inex_id_student)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __run_srv_cauta_student_tests(self):
        repo = RepoStudent()
        valid_student = ValidatorStudent()
        srv = ServiceStudent(valid_student,repo)
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        id_student_cautat = 1023
        id_cautat_invalid = -23
        inex_id_student = 222
        self.__test_cauta_student_srv_succes(srv,id_student,nume,grupa,id_student_cautat)
        self.__test_cauta_student_srv_id_cautat_invalid(srv,id_cautat_invalid)
        self.__test_cauta_student_srv_id_inexistent(srv,inex_id_student)
    
    
    
    
    def __test_creeaza_lab_succes(self, nrlab_nrpb, descriere, deadline):
        lab = Laborator(nrlab_nrpb,descriere,deadline)
        assert (lab.get_nrlab_nrpb()==nrlab_nrpb)
        assert(lab.get_descriere()==descriere)
        assert(lab.get_deadline()==deadline)
        return lab
    
    
    def __test_egalitate_lab(self, lab, nrlab_nrpb, alta_descriere, alt_deadline):
        alt_lab_acelasi_id = Laborator(nrlab_nrpb,alta_descriere,alt_deadline)
        assert(alt_lab_acelasi_id == lab)
    
    
    def __test_printeaza_lab(self, lab):
        assert (str(lab)=="Numar laborator_numar problema: 7_2 Descrierea: Laboratorul 7, problema 2 - cu studenti si laboratoare. Deadline-ul: 12.11.2021.")
    
    
    def __run_creeaza_lab_test(self):
        nrlab_nrpb = "7_2"
        descriere = "Laboratorul 7, problema 2 - cu studenti si laboratoare."
        deadline = [12,11,2021]
        lab = self.__test_creeaza_lab_succes(nrlab_nrpb, descriere, deadline)
        alta_descriere = "Laboratorul 7, problema 2 - cu filme si actori."
        alt_deadline = [23,11,2021]
        self.__test_egalitate_lab(lab,nrlab_nrpb,alta_descriere,alt_deadline)
        self.__test_printeaza_lab(lab)
    
    
    def __test_valideaza_lab_succes(self, nrlab_nrpb, descriere, deadline):
        lab = Laborator(nrlab_nrpb,descriere,deadline)
        valid_lab = ValidatorLaborator()
        valid_lab.valideaza(lab)
        assert (True)
        return valid_lab
    
    
    def __test_valideaza_lab_id_invalid(self, valid_lab, inv_nrlab_nrpb, descriere, deadline):
        lab = Laborator(inv_nrlab_nrpb,descriere,deadline)
        try:
            valid_lab.valideaza(lab)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Id invalid!\n")
    
    
    def __test_valideaza_lab_descriere_invalid(self, valid_lab, nrlab_nrpb, inv_descriere, deadline):
        lab = Laborator(nrlab_nrpb,inv_descriere,deadline)
        try:
            valid_lab.valideaza(lab)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Descriere invalida!\n")
    
    
    def __test_valideaza_lab_deadline_invalida(self, valid_lab, nrlab_nrpb, descriere, inv_deadline):
        lab = Laborator(nrlab_nrpb,descriere,inv_deadline)
        try:
            valid_lab.valideaza(lab)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Deadline invalid!\n")
    
    
    def __test_valideaza_lab_toate_invalide(self, valid_lab, inv_nrlab_nrpb, inv_descriere, inv_deadline):
        lab = Laborator(inv_nrlab_nrpb,inv_descriere,inv_deadline)
        try:
            valid_lab.valideaza(lab)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Id invalid!\nDescriere invalida!\nDeadline invalid!\n")
    
    
    def __run_valideaza_lab_tests(self):
        nrlab_nrpb = "7_2"
        descriere = "Laboratorul 7, problema 2 - cu studenti si laboratoare."
        deadline = [12,11,2021]
        valid_lab = self.__test_valideaza_lab_succes(nrlab_nrpb,descriere,deadline)
        inv_nrlab_nrpb = ""
        inv_descriere = ""
        inv_deadline = [40,13,2000]
        self.__test_valideaza_lab_id_invalid(valid_lab,inv_nrlab_nrpb,descriere,deadline)
        self.__test_valideaza_lab_descriere_invalid(valid_lab,nrlab_nrpb,inv_descriere,deadline)
        self.__test_valideaza_lab_deadline_invalida(valid_lab,nrlab_nrpb,descriere,inv_deadline)
        self.__test_valideaza_lab_toate_invalide(valid_lab,inv_nrlab_nrpb,inv_descriere,inv_deadline)
    
    
    def __test_creeaza_repo_lab_vid(self):
        file_path = "tests/test_laboratoare.txt"
        with open(file_path,"w") as f:
            f.write("")
        repo = FileRepoLaborator(file_path)
        assert(len(repo)==0)
        return repo
    
    
    def __test_adauga_lab_repo_succes(self, repo, lab):
        repo.adauga_lab(lab)
        assert(len(repo)==1)
    
    
    def __test_cauta_lab_id_inexistent(self, repo, inex_nrlab_nrpb):
        try:
            repo.cauta_lab_dupa_id(inex_nrlab_nrpb)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __test_adauga_lab_acelasi_id(self, repo, alt_lab_acelasi_id):
        try:
            repo.adauga_lab(alt_lab_acelasi_id)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id lab existent!\n")
    
    
    def __run_repo_adauga_lab_tests(self):
        repo = self.__test_creeaza_repo_lab_vid()
        nrlab_nrpb = "7_2"
        descriere = "Laboratorul 7 problema 2 - cu studenti si laboratoare."
        deadline = [12,11,2021]
        lab = Laborator(nrlab_nrpb,descriere,deadline)
        self.__test_adauga_lab_repo_succes(repo,lab)
        lab_gasit = repo.cauta_lab_dupa_id(nrlab_nrpb)
        assert(lab.get_descriere()==lab_gasit.get_descriere())
        assert(lab.get_deadline()==lab_gasit.get_deadline())
        inex_nrlab_nrpb = "8_2"
        alta_descriere = "Laboratorul 7 problema 2 - cu filme si actori."
        alt_deadline = [17,11,2021]
        self.__test_cauta_lab_id_inexistent(repo,inex_nrlab_nrpb)
        alt_lab_acelasi_id = Laborator(nrlab_nrpb,alta_descriere,alt_deadline)
        self.__test_adauga_lab_acelasi_id(repo,alt_lab_acelasi_id)
    
    
    def __test_adauga_lab_srv_succes(self, srv, nrlab_nrpb, descriere, deadline):
        all_el = srv.get_all_lab()
        assert(len(all_el)==0)
        srv.adauga_lab(nrlab_nrpb,descriere,deadline)
        all_el = srv.get_all_lab()
        assert(len(all_el)==1)
        assert(all_el[0].get_nrlab_nrpb()==nrlab_nrpb)
        assert(all_el[0].get_descriere()==descriere)
        assert(all_el[0].get_deadline()==deadline)
    
    
    def __test_adauga_lab_srv_id_invalid(self, srv, inv_nrlab_nrpb, descriere, deadline):
        try:
            srv.adauga_lab(inv_nrlab_nrpb,descriere,deadline)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id invalid!\n")
    
    
    def __test_adauga_lab_srv_descriere_invalid(self, srv, nrlab_nrpb, inv_descriere, deadline):
        try:
            srv.adauga_lab(nrlab_nrpb,inv_descriere,deadline)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Descriere invalida!\n")
    
    
    def __test_adauga_lab_srv_deadline_invalida(self, srv, nrlab_nrpb, descriere, inv_deadline):
        try:
            srv.adauga_lab(nrlab_nrpb,descriere,inv_deadline)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Deadline invalid!\n")
    
    def __test_adauga_lab_srv_toate_invalide(self, srv, inv_nrlab_nrpb, inv_descriere, inv_deadline):
        try:
            srv.adauga_lab(inv_nrlab_nrpb,inv_descriere,inv_deadline)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id invalid!\nDescriere invalida!\nDeadline invalid!\n")
    
    
    def __run_srv_adauga_lab_tests(self):
        repo = RepoLaborator()
        valid_lab = ValidatorLaborator()
        srv = ServiceLaborator(valid_lab,repo)
        nrlab_nrpb = "7_2"
        descriere = "Laboratorul 7 problema 2 - cu studenti si laboratoare."
        deadline = [12,11,2021]
        inv_nrlab_nrpb = ""
        inv_descriere = ""
        inv_deadline = [40,13,2000]
        self.__test_adauga_lab_srv_succes(srv,nrlab_nrpb,descriere,deadline)
        self.__test_adauga_lab_srv_id_invalid(srv,inv_nrlab_nrpb,descriere,deadline)
        self.__test_adauga_lab_srv_descriere_invalid(srv,nrlab_nrpb,inv_descriere,deadline)
        self.__test_adauga_lab_srv_deadline_invalida(srv,nrlab_nrpb,descriere,inv_deadline)
        self.__test_adauga_lab_srv_toate_invalide(srv,inv_nrlab_nrpb,inv_descriere,inv_deadline)
    

    def __test_sterge_lab_repo_succes(self, repo, lab):
        repo.adauga_lab(lab)
        assert(len(repo)==1)
        nrlab_nrpb = lab.get_nrlab_nrpb()
        repo.sterge_lab_dupa_id(nrlab_nrpb)
        assert(len(repo)==0)
    
    
    def __test_sterge_lab_repo_id_inexistent(self, repo, lab, inex_nrlab_nrpb):
        repo.adauga_lab(lab)
        assert(len(repo)==1)
        try:
            repo.sterge_lab_dupa_id(inex_nrlab_nrpb)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_repo_sterge_lab_tests(self):
        repo = self.__test_creeaza_repo_lab_vid()
        lab = Laborator("7_2","Gabi",[12,12,2021])
        self.__test_sterge_lab_repo_succes(repo,lab)
        inex_nrlab_nrpb = ""
        self.__test_sterge_lab_repo_id_inexistent(repo, lab, inex_nrlab_nrpb)
    
    
    def __test_sterge_lab_srv_succes(self, srv, nrlab_nrpb, descriere, deadline):
        all_el = srv.get_all_lab()
        assert(len(all_el)==0)
        srv.adauga_lab(nrlab_nrpb,descriere,deadline)
        all_el = srv.get_all_lab()
        assert(len(all_el)==1)
        assert(all_el[0].get_nrlab_nrpb()==nrlab_nrpb)
        assert(all_el[0].get_descriere()==descriere)
        assert(all_el[0].get_deadline()==deadline)
        srv.sterge_lab(nrlab_nrpb)
        all_el = srv.get_all_lab()
        assert(len(all_el)==0)
    
    
    def __test_sterge_lab_srv_id_inexistent(self, srv, nrlab_nrpb, inex_nrlab_nrpb, descriere, deadline):
        srv.adauga_lab(nrlab_nrpb,descriere,deadline)
        try:
            srv.sterge_lab(inex_nrlab_nrpb)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_srv_sterge_lab_tests(self):
        repo = RepoLaborator()
        valid_lab = ValidatorLaborator()
        srv = ServiceLaborator(valid_lab,repo)
        nrlab_nrpb = "7_2"
        descriere = "Laboratorul 7 problema 2 - cu studenti si laboratoare."
        deadline = [12,11,2021]
        inex_nrlab_nrpb = "7_11"
        self.__test_sterge_lab_srv_succes(srv,nrlab_nrpb,descriere,deadline)
        self.__test_sterge_lab_srv_id_inexistent(srv,nrlab_nrpb,inex_nrlab_nrpb,descriere,deadline)
    
    
    def __test_modifica_descriere_lab_repo_succes(self, repo, lab,descriere_noua):
        repo.adauga_lab(lab)
        assert(len(repo)==1)
        nrlab_nrpb = lab.get_nrlab_nrpb()
        repo.modifica_descriere_lab(nrlab_nrpb,descriere_noua)
        lab = repo.cauta_lab_dupa_id(nrlab_nrpb)
        assert(lab.get_descriere()==descriere_noua)
    
    
    def __test_modifica_descriere_lab_repo_id_inexistent(self, repo, descriere_noua, inex_nrlab_nrpb):
        assert(len(repo)==1)
        try:
            repo.modifica_descriere_lab(inex_nrlab_nrpb,descriere_noua)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_repo_modifica_descriere_lab_tests(self):
        repo = self.__test_creeaza_repo_lab_vid()
        lab = Laborator("7_2","Gabi",[13,12,2022])
        descriere_noua = "Laboratorul 7 problema 2 - cu filme si actori."
        self.__test_modifica_descriere_lab_repo_succes(repo,lab,descriere_noua)
        inex_nrlab_nrpb = "7_0"
        self.__test_modifica_descriere_lab_repo_id_inexistent(repo, descriere_noua, inex_nrlab_nrpb)
    
    
    def __test_modifica_descriere_lab_srv_succes(self, srv, nrlab_nrpb, descriere, deadline, descriere_noua):
        srv.adauga_lab(nrlab_nrpb,descriere,deadline)
        all_el = srv.get_all_lab()
        assert(all_el[0].get_descriere()==descriere)
        srv.modifica_descriere_lab(nrlab_nrpb,descriere_noua)
        all_el = srv.get_all_lab()
        assert(all_el[0].get_descriere()==descriere_noua)
    
    
    def __test_modifica_descriere_lab_srv_descriere_invalid(self, srv, nrlab_nrpb, inv_descriere):
        try:
            srv.modifica_descriere_lab(nrlab_nrpb,inv_descriere)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Descriere invalida!\n")
    
    
    def __test_modifica_descriere_lab_srv_id_inexistent(self, srv, inex_nrlab_nrpb, descriere_noua):
        try:
            srv.modifica_descriere_lab(inex_nrlab_nrpb,descriere_noua)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_srv_modifica_descriere_lab_tests(self):
        repo = RepoLaborator()
        valid_lab = ValidatorLaborator()
        srv = ServiceLaborator(valid_lab,repo)
        nrlab_nrpb = "7_2"
        descriere = "Laboratorul 7 problema 2 - cu studenti si laboratoare."
        deadline = [12,11,2021]
        inex_nrlab_nrpb = "2_3"
        descriere_noua = "Laboratorul 7 problema 2 - cu filme si actori."
        inv_descriere = ""
        self.__test_modifica_descriere_lab_srv_succes(srv,nrlab_nrpb,descriere,deadline,descriere_noua)
        self.__test_modifica_descriere_lab_srv_descriere_invalid(srv,nrlab_nrpb,inv_descriere)
        self.__test_modifica_descriere_lab_srv_id_inexistent(srv,inex_nrlab_nrpb,descriere_noua)
        
        
    def __test_modifica_deadline_lab_repo_succes(self, repo, lab,deadline_nou):
        repo.adauga_lab(lab)
        assert(len(repo)==1)
        nrlab_nrpb = lab.get_nrlab_nrpb()
        repo.modifica_deadline_lab(nrlab_nrpb,deadline_nou)
        lab = repo.cauta_lab_dupa_id(nrlab_nrpb)
        assert(lab.get_deadline()==deadline_nou)
    
    
    def __test_modifica_deadline_lab_repo_id_inexistent(self, repo, deadline_nou, inex_nrlab_nrpb):
        assert(len(repo)==1)
        try:
            repo.modifica_deadline_lab(inex_nrlab_nrpb,deadline_nou)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_repo_modifica_deadline_lab_tests(self):
        repo = self.__test_creeaza_repo_lab_vid()
        lab = Laborator("7_2","Gabi",[12,11,2021])
        deadline_nou = [15,11,2021]
        self.__test_modifica_deadline_lab_repo_succes(repo,lab,deadline_nou)
        inex_nrlab_nrpb = ""
        self.__test_modifica_deadline_lab_repo_id_inexistent(repo, deadline_nou, inex_nrlab_nrpb)
    
    
    def __test_modifica_deadline_lab_srv_succes(self, srv, nrlab_nrpb, descriere, deadline, deadline_nou):
        srv.adauga_lab(nrlab_nrpb,descriere,deadline)
        all_el = srv.get_all_lab()
        assert(all_el[0].get_descriere()==descriere)
        srv.modifica_deadline_lab(nrlab_nrpb,deadline_nou)
        all_el = srv.get_all_lab()
        assert(all_el[0].get_deadline()==deadline_nou)
    
    
    def __test_modifica_deadline_lab_srv_descriere_invalid(self, srv, nrlab_nrpb, inv_deadline):
        try:
            srv.modifica_deadline_lab(nrlab_nrpb,inv_deadline)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Deadline invalid!\n")
    
    
    def __test_modifica_deadline_lab_srv_id_inexistent(self, srv, inex_nrlab_nrpb, deadline_nou):
        try:
            srv.modifica_deadline_lab(inex_nrlab_nrpb,deadline_nou)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_srv_modifica_deadline_lab_tests(self):
        repo = RepoLaborator()
        valid_lab = ValidatorLaborator()
        srv = ServiceLaborator(valid_lab,repo)
        nrlab_nrpb = "7_2"
        descriere = "Laboratorul 7 problema 2 - cu studenti si laboratoare."
        deadline = [12,11,2021]
        inex_nrlab_nrpb = ""
        deadline_nou = [15,11,2021]
        inv_deadline = []
        self.__test_modifica_deadline_lab_srv_succes(srv,nrlab_nrpb,descriere,deadline,deadline_nou)
        self.__test_modifica_deadline_lab_srv_descriere_invalid(srv,nrlab_nrpb,inv_deadline)
        self.__test_modifica_deadline_lab_srv_id_inexistent(srv,inex_nrlab_nrpb,deadline_nou)
    
    
    def __test_cauta_lab_repo_succes(self, repo, lab, nrlab_nrpb_cautat):
        repo.adauga_lab(lab)
        assert(len(repo)==1)
        lb = repo.cauta_lab_dupa_id(nrlab_nrpb_cautat)
        assert(lb.get_nrlab_nrpb()==lab.get_nrlab_nrpb())
        assert(lb.get_descriere()==lab.get_descriere())
        assert(lb.get_deadline()==lab.get_deadline())
    
    def __test_cauta_lab_repo_id_inexistent(self, repo, inex_nrlab_nrpb):
        assert(len(repo)==1)
        try:
            repo.cauta_lab_dupa_id(inex_nrlab_nrpb)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
            
    def __run_repo_cauta_lab_tests(self):
        repo = self.__test_creeaza_repo_lab_vid()
        lab = Laborator("1_2","Atentie mare la detalii.",[15,11,2021])
        nrlab_nrpb_cautat = "1_2"
        self.__test_cauta_lab_repo_succes(repo,lab,nrlab_nrpb_cautat)
        inex_nrlab_nrpb = "1_5"
        self.__test_cauta_lab_repo_id_inexistent(repo, inex_nrlab_nrpb)
    
    
    def __test_cauta_lab_srv_succes(self, srv, nrlab_nrpb, descriere, deadline, nrlab_nrpb_cautat):
        srv.adauga_lab(nrlab_nrpb,descriere,deadline)
        gasit = srv.cauta_lab(nrlab_nrpb_cautat)
        assert(gasit.get_nrlab_nrpb()==nrlab_nrpb)
        assert(gasit.get_descriere()==descriere)
        assert(gasit.get_deadline()==deadline)
    
    
    def __test_cauta_lab_srv_id_cautat_invalid(self, srv, id_cautat_invalid):
        try:
            srv.cauta_lab(id_cautat_invalid)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id invalid!\n")
    
    
    def __test_cauta_lab_srv_id_inexistent(self, srv, inex_nrlab_nrpb):
        try:
            srv.cauta_lab(inex_nrlab_nrpb)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_srv_cauta_lab_tests(self):
        repo = RepoLaborator()
        valnrlab_nrpb = ValidatorLaborator()
        srv = ServiceLaborator(valnrlab_nrpb,repo)
        nrlab_nrpb = "1_2" 
        descriere = "Atentie la detalii"
        deadline = [12,11,2021]
        nrlab_nrpb_cautat = "1_2"
        id_cautat_invalid = ""
        inex_nrlab_nrpb = "1_5"
        self.__test_cauta_lab_srv_succes(srv,nrlab_nrpb,descriere,deadline,nrlab_nrpb_cautat)
        self.__test_cauta_lab_srv_id_cautat_invalid(srv,id_cautat_invalid)
        self.__test_cauta_lab_srv_id_inexistent(srv,inex_nrlab_nrpb)



    def __test_filtreaza_dupa_prefix_repo_succes(self,repo,student, prefix):
        repo.adauga_student(student)
        assert(len(repo)==1)
        studenti = repo.filtreaza_dupa_prefix_nume(prefix)
        assert(studenti[0].get_id_student()==student.get_id_student())
        assert(studenti[0].get_nume()==student.get_nume())
        assert(abs(studenti[0].get_grupa()-student.get_grupa())<0.0001)
    
    
    def __test_filtreaza_dupa_prefix_repo_inexistent(self, repo, prefix_inex):
        assert(len(repo)==1)
        try:
            repo.filtreaza_dupa_prefix_nume(prefix_inex)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Nu exista studenti care sa aiba prefix sirul citit!\n")
    
    
    def __run_repo_filtreaza_dupa_prefix_tests(self):
        repo = self.__test_creeaza_repo_vid()
        id_student = 12
        nume = "George Pop"
        grupa = 214
        student = Student(id_student,nume,grupa)
        prefix = "Ge"
        self.__test_filtreaza_dupa_prefix_repo_succes(repo, student,prefix)
        prefix_inex = "Ra"
        self.__test_filtreaza_dupa_prefix_repo_inexistent(repo, prefix_inex)
  
  
    def __test_filtreaza_dupa_prefix_srv_succes(self, srv, id_student, nume, grupa, prefix):
        srv.adauga_student(id_student,nume,grupa)
        studenti = srv.filtreaza_nume(prefix)
        assert(studenti[0].get_id_student()==id_student)
        assert(studenti[0].get_nume()==nume)
        assert(abs(studenti[0].get_grupa()-grupa)<0.0001)
    
    
    def __test_filtreaza_dupa_prefix_srv_prefix_invalid(self, srv, prefix_inv):
        try:
            srv.filtreaza_nume(prefix_inv)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Prefix invalid!\n")
    
    
    def __test_filtreaza_dupa_prefix_srv_prefix_negasit(self, srv,prefix_inex):
        try:
            srv.filtreaza_nume(prefix_inex)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Nu exista studenti care sa aiba prefix sirul citit!\n")
    
    
    def __run_srv_filtreaza_dupa_prefix_tests(self):
        repo = RepoStudent()
        valid_student = ValidatorStudent()
        srv = ServiceStudent(valid_student,repo)
        id_student = 1023
        nume = "Mircea Farcas"
        grupa = 213
        prefix = "Mi"
        prefix_inv =""
        prefix_inex = "Ge"
        self.__test_filtreaza_dupa_prefix_srv_succes(srv,id_student,nume,grupa,prefix)
        self.__test_filtreaza_dupa_prefix_srv_prefix_invalid(srv,prefix_inv)
        self.__test_filtreaza_dupa_prefix_srv_prefix_negasit(srv,prefix_inex)
    
    
    
    
    def __test_creeaza_nota_succes(self, id_nota, student, lab):
        nota = Note(id_nota,student,lab)
        assert(nota.get_id_nota()==id_nota)
        assert(nota.get_student()==student)
        assert(nota.get_lab()==lab)
        return nota
    
    
    def __test_egalitate_nota(self, nota, id_nota, alt_student, lab):
        alta_nota_acelasi_id = Note(id_nota,alt_student,lab)
        assert(alta_nota_acelasi_id == nota)
    
    
    def __run_creeaza_nota_test(self):
        id_nota = 1
        student = Student(1,"Gabi",213)
        lab = Laborator("1_1","usor",[13,12,2021])
        nota = self.__test_creeaza_nota_succes(id_nota,student,lab)
        alt_student = Student(2,"Nicu",213)
        self.__test_egalitate_nota(nota,id_nota,alt_student,lab)
    
    def __test_valideaza_nota_asignata_succes(self, id_nota,student,lab):
        nota = Note(id_nota,student,lab)
        valid_note = ValidatorNote()
        valid_note.valideaza_id(nota)
        assert (True)
        return valid_note
    
    
    def __test_valideaza_nota_asignata_id_invalid(self, valid_nota, inv_id_nota,student,lab):
        nota = Note(inv_id_nota,student,lab)
        try:
            valid_nota.valideaza_id(nota)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Id invalid!\n")
    
    
    def __test_valideaza_nota_adaugata_succes(self, id_nota,student,lab, nota_pb):
        nota = Note(id_nota,student,lab)
        valid_note = ValidatorNote()
        valid_note.valideaza(nota,nota_pb)
        assert (True)
        return valid_note
    
    def __test_valideaza_nota_adaugata_invalida(self, valid_nota_adaugata, id_nota,student,lab, inv_nota_pb):
        nota = Note(id_nota,student,lab)
        try:
            valid_nota_adaugata.valideaza(nota,inv_nota_pb)
            assert(False)
        except ValidationError as ve:
            assert (str(ve)=="Nota invalida!\n")
            
    def __run_valideaza_nota_tests(self):
        id_nota = 12
        student = Student(1,"Gabi",213)
        lab = Laborator("1_1","usor",[13,12,2021])
        valid_nota = self.__test_valideaza_nota_asignata_succes(id_nota,student,lab)
        inv_id_nota = -32
        self.__test_valideaza_nota_asignata_id_invalid(valid_nota,inv_id_nota,student,lab)
        nota_pb = 9.5
        valid_nota_adaugata = self.__test_valideaza_nota_adaugata_succes(id_nota,student,lab, nota_pb)
        inv_nota_pb = 0.4
        self.__test_valideaza_nota_adaugata_invalida(valid_nota_adaugata, id_nota, student, lab, inv_nota_pb)
    
    
    def __test_creeaza_repo_nota_vid(self):
        file_path = "tests/test_note.txt"
        with open(file_path,"w") as f:
            f.write("")
        repo = FileRepoNote(file_path)
        assert(len(repo)==0)
        return repo
    
    
    def __test_asignare_nota_repo_succes(self, repo, nota):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
    
    
    def __test_cauta_nota_id_inexistent(self, repo, inex_id_nota):
        try:
            repo.cauta_nota_dupa_id(inex_id_nota)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id nota laborator inexistent!\n")
    
    
    def __test_asignare_nota_acelasi_id(self, repo, alta_nota_acelasi_id):
        try:
            repo.asignare_nota(alta_nota_acelasi_id)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Problema de laborator deja asignata!\n")
    
    
    def __test_asignare_nota_problema_deja_asignata(self, repo,problema_deja_asignata):
        try:
            repo.asignare_nota(problema_deja_asignata)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Problema de laborator deja asignata!\n")
    
    
    def __run_repo_asignare_nota_tests(self):
        repo = self.__test_creeaza_repo_nota_vid()
        id_nota = 12
        student = Student(1,"Gabi",213)
        lab = Laborator("1_1","usor",[13,12,2021])
        nota = NoteDTO(id_nota,student.get_id_student(),lab.get_nrlab_nrpb(),0)
        self.__test_asignare_nota_repo_succes(repo,nota)
        inex_id_nota = 155
        alt_student = Student(2,"Nicu",213)
        self.__test_cauta_nota_id_inexistent(repo,inex_id_nota)
        alta_nota_acelasi_id = NoteDTO(id_nota,alt_student.get_id_student(),lab.get_nrlab_nrpb(),0)
        problema_deja_asignata = NoteDTO(14,student.get_id_student(),lab.get_nrlab_nrpb(),0)
        self.__test_asignare_nota_acelasi_id(repo,alta_nota_acelasi_id)
        self.__test_asignare_nota_problema_deja_asignata(repo,problema_deja_asignata)
    
    
    def __test_asignare_nota_srv_succes(self, srv,srv_student,srv_lab, id_nota, id_student, nrlab_nrpb):
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==0)
        srv_student.adauga_student(1,"Tudor",213)
        srv_lab.adauga_lab("1_1","ceva",[12,12,2021])
        srv.asignare_nota(id_nota, id_student, nrlab_nrpb)
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==1)
        assert(all_el[0].get_id_nota()==id_nota)
        assert(all_el[0].get_id_student()==id_student)
        assert(all_el[0].get_nrlab_nrpb()==nrlab_nrpb)
        assert(all_el[0].get_nota()==0)
    
    
    def __test_asignare_nota_srv_id_invalid(self, srv, inv_id_nota, id_student, nrlab_nrpb):
        try:
            srv.asignare_nota(inv_id_nota,id_student,nrlab_nrpb)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id invalid!\n")
    
    def __run_srv_asignare_nota_tests(self):
        repo_note = RepoNote()
        valid_note = ValidatorNote()
        repo_student = RepoStudent()
        repo_lab = RepoLaborator()
        srv = ServiceNote(valid_note,repo_note,repo_student,repo_lab)
        valid_student = ValidatorStudent()
        valid_lab = ValidatorLaborator()
        srv_student = ServiceStudent(valid_student,repo_student)
        srv_lab = ServiceLaborator(valid_lab,repo_lab)
        id_nota = 1
        id_student = 1
        nrlab_nrpb = "1_1"
        inv_id_nota = -2
        self.__test_asignare_nota_srv_succes(srv,srv_student,srv_lab,id_nota,id_student,nrlab_nrpb)
        self.__test_asignare_nota_srv_id_invalid(srv, inv_id_nota, id_student, nrlab_nrpb)
    
 
    def __test_adaugare_nota_repo_succes(self, repo, nota, nota_pb):
        repo.asignare_nota(nota)
        repo.adauga_nota(nota,nota_pb)
        assert(len(repo)==1)
        assert(repo.cauta_nota_dupa_id(nota.get_id_nota()).get_nota()==nota_pb)
    
    
    
    def __run_repo_adaugare_nota_tests(self):
        repo = self.__test_creeaza_repo_nota_vid()
        id_nota = 12
        student = Student(1,"Gabi",213)
        lab = Laborator("1_1","usor",[13,12,2021])
        nota_pb = 9.5
        nota = NoteDTO(id_nota,student.get_id_student(),lab.get_nrlab_nrpb(),0)
        self.__test_adaugare_nota_repo_succes(repo,nota,nota_pb)
        nota_gasita = repo.cauta_nota_dupa_id(id_nota)
        assert(nota.get_student()==nota_gasita.get_student())
        assert(nota.get_lab()==nota_gasita.get_lab())
        assert(repo.cauta_nota_dupa_id(nota.get_id_nota()).get_nota()==repo.cauta_nota_dupa_id(nota_gasita.get_id_nota()).get_nota())
        inex_id_nota = 155
        self.__test_cauta_nota_id_inexistent(repo,inex_id_nota)
    
    
    def __test_adaugare_nota_srv_succes(self, srv,srv_student,srv_lab, id_nota,nota_pb):
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==0)
        srv_student.adauga_student(1,"Tudor",213)
        srv_lab.adauga_lab("1_1","ceva",[12,12,2021])
        srv.asignare_nota(id_nota,1,"1_1")
        srv_lab.adauga_lab("1_5","ceva",[12,12,2021])
        srv.adauga_nota(id_nota,nota_pb)
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==1)
        assert(all_el[0].get_id_nota()==id_nota)
        assert(all_el[0].get_id_student()==1)
        assert(all_el[0].get_nrlab_nrpb()=="1_1")
        assert(all_el[0].get_nota()==nota_pb)
        
        
    def __test_adaugare_nota_srv_id_inexistent(self, srv, inex_id_nota,nota_pb):
        try:
            srv.asignare_nota(13,1,"1_5")
            srv.adauga_nota(inex_id_nota,nota_pb)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id nota laborator inexistent!\n")
    
    
    def __run_srv_adaugare_nota_tests(self):
        repo_note = RepoNote()
        valid_note = ValidatorNote()
        repo_student = RepoStudent()
        repo_lab = RepoLaborator()
        srv = ServiceNote(valid_note,repo_note,repo_student,repo_lab)
        valid_student = ValidatorStudent()
        valid_lab = ValidatorLaborator()
        srv_student = ServiceStudent(valid_student,repo_student)
        srv_lab = ServiceLaborator(valid_lab,repo_lab)
        id_nota = 1
        nota_pb = 9.5
        inex_id_nota = -2
        self.__test_adaugare_nota_srv_succes(srv,srv_student,srv_lab,id_nota,nota_pb)
        self.__test_adaugare_nota_srv_id_inexistent(srv, inex_id_nota,nota_pb)
    
    
    def __test_sterge_nota_repo_succes(self, repo, nota):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
        id_nota = nota.get_id_nota()
        repo.sterge_nota_dupa_id_nota(id_nota)
        assert(len(repo)==0)
    
    
    def __test_sterge_nota_repo_id_inexistent(self, repo, nota, inex_id_nota):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
        try:
            repo.sterge_nota_dupa_id_nota(inex_id_nota)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id nota laborator inexistent!\n")
    
        
    def __test_sterge_nota_repo_dupa_id_student_succes(self, repo, nota, id_student):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
        repo.sterge_nota_dupa_id_student(id_student)
        assert(len(repo)==0)
    
    
    def __test_sterge_nota_repo_dupa_id_student_inexistent(self, repo, nota, id_student_inex):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
        try:
            repo.sterge_nota_dupa_id_student(id_student_inex)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __test_sterge_nota_repo_dupa_id_lab_succes(self, repo, nota, id_lab):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
        repo.sterge_nota_dupa_id_lab(id_lab)
        assert(len(repo)==0)
    
    
    def __test_sterge_nota_repo_dupa_id_lab_inexistent(self, repo, nota, id_lab_inex):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
        try:
            repo.sterge_nota_dupa_id_lab(id_lab_inex)
            assert (False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_repo_sterge_nota_tests(self):
        repo = self.__test_creeaza_repo_nota_vid()
        id_nota = 12
        student = Student(1,"Gabi",213)
        lab = Laborator("1_1","usor",[13,12,2021])
        nota = NoteDTO(id_nota,student.get_id_student(),lab.get_nrlab_nrpb(),0)
        self.__test_sterge_nota_repo_succes(repo, nota)
        inex_id_nota = 3433
        self.__test_sterge_nota_repo_id_inexistent(repo, nota, inex_id_nota)
        repo.sterge_nota_dupa_id_nota(nota.get_id_nota())
        id_nota2 = 91
        student2 = Student(2,"Tomas",212)
        lab2 = Laborator("1_2","greu",[19,11,2021])
        nota2 = NoteDTO(id_nota2,student2.get_id_student(),lab2.get_nrlab_nrpb(),0)
        id_student2 = student2.get_id_student()
        self.__test_sterge_nota_repo_dupa_id_student_succes(repo,nota2,id_student2)
        id_student_inex = 32
        self.__test_sterge_nota_repo_dupa_id_student_inexistent(repo,nota2,id_student_inex)
        repo.sterge_nota_dupa_id_nota(nota2.get_id_nota())
        id_nota3 = 91
        student3 = Student(3,"Luca",217)
        lab3 = Laborator("1_3","mediu",[22,12,2021])
        nota3 = NoteDTO(id_nota3,student3.get_id_student(),lab3.get_nrlab_nrpb(),0)
        id_lab = lab3.get_nrlab_nrpb()
        self.__test_sterge_nota_repo_dupa_id_lab_succes(repo,nota3,id_lab)
        id_lab_inex = "1_2132"
        self.__test_sterge_nota_repo_dupa_id_lab_inexistent(repo,nota3,id_lab_inex)
    
    
    def __test_sterge_nota_srv_succes(self,srv,srv_student,srv_lab,id_nota,nota_pb):        
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==0)
        srv_student.adauga_student(1,"Tudor",213)
        srv_lab.adauga_lab("1_1","ceva",[12,12,2021])
        srv.asignare_nota(id_nota,1,"1_1")
        srv.adauga_nota(id_nota,nota_pb)
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==1)
        srv.sterge_nota(id_nota)
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==0)
        

    def __test_sterge_nota_srv_id_inexistent(self, srv, id_nota, inex_id_nota, nota_pb):
        try:
            srv.asignare_nota(id_nota,1,"1_1")
            srv.adauga_nota(id_nota,nota_pb)
            srv.sterge_nota(inex_id_nota)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id nota laborator inexistent!\n")
    
    
    def __test_sterge_nota_srv_id_invalid(self, srv, id_nota, inv_id_nota, nota_pb):
        try:
            srv.asignare_nota(id_nota,1,"1_1")
            srv.adauga_nota(id_nota,nota_pb)
            srv.sterge_nota(inv_id_nota)
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id nota laborator invalid!\n")
    
    def __test_sterge_nota_dupa_id_student_srv_succes(self, srv, srv_student, srv_lab, id_nota, nota_pb):
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==0)
        srv_student.adauga_student(101,"Tudor",213)
        srv_lab.adauga_lab("1_4","ceva",[12,12,2021])
        srv.asignare_nota(id_nota,101,"1_4")
        srv.adauga_nota(id_nota,nota_pb)
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==1)
        srv.sterge_student_nota(101)
        assert(len(all_el)==0)
    
    
    def __test_sterge_nota_dupa_id_student_inexistent_srv_succes(self, srv,  id_nota, nota_pb):
        try:
            srv.asignare_nota(id_nota,101,"1_4")
            srv.adauga_nota(id_nota,nota_pb)
            srv.sterge_student_nota(1923)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id student inexistent!\n")
    
    
    def __test_sterge_nota_dupa_id_lab_srv_succes(self, srv, srv_student, srv_lab, id_nota, nota_pb):
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==0)
        srv_student.adauga_student(43,"Tudorica",213)
        srv_lab.adauga_lab("2_4","cevaasdf",[12,12,2021])
        srv.asignare_nota(id_nota,43,"2_4")
        srv.adauga_nota(id_nota,nota_pb)
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==1)
        srv.sterge_lab_nota("2_4")
        assert(len(all_el)==0)
    
    
    def __test_sterge_nota_dupa_id_lab_inexistent_srv_succes(self, srv, id_nota, nota_pb):
        try:
            srv.asignare_nota(id_nota,43,"2_4")
            srv.adauga_nota(id_nota,nota_pb)
            srv.sterge_lab_nota("1_111")
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Id lab inexistent!\n")
    
    
    def __run_srv_sterge_nota_tests(self):
        repo_note = RepoNote()
        valid_note = ValidatorNote()
        repo_student = RepoStudent()
        repo_lab = RepoLaborator()
        srv = ServiceNote(valid_note,repo_note,repo_student,repo_lab)
        valid_student = ValidatorStudent()
        valid_lab = ValidatorLaborator()
        srv_student = ServiceStudent(valid_student,repo_student)
        srv_lab = ServiceLaborator(valid_lab,repo_lab)
        id_nota = 1
        nota_pb = 9.5
        inex_id_nota = 999
        inv_id_nota = -2
        self.__test_sterge_nota_srv_succes(srv,srv_student,srv_lab,id_nota,nota_pb)
        self.__test_sterge_nota_srv_id_inexistent(srv, id_nota, inex_id_nota, nota_pb)
        srv.sterge_nota(id_nota)
        self.__test_sterge_nota_srv_id_invalid(srv, id_nota, inv_id_nota, nota_pb)
        srv.sterge_nota(id_nota)
        self.__test_sterge_nota_dupa_id_student_srv_succes(srv,srv_student,srv_lab,id_nota,nota_pb)
        self.__test_sterge_nota_dupa_id_student_inexistent_srv_succes(srv,id_nota,nota_pb)
        srv.sterge_nota(id_nota)
        self.__test_sterge_nota_dupa_id_lab_srv_succes(srv,srv_student,srv_lab,id_nota,nota_pb)
        self.__test_sterge_nota_dupa_id_lab_inexistent_srv_succes(srv,id_nota,nota_pb)
    
    
    def __test_get_all_note_dupa_id_problema_repo_succes(self, repo, nrlab_nrpb, nota,nota2,nota_pb):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
        repo.adauga_nota(nota,nota_pb)
        note_anumita_problema = repo.get_all_note_dupa_id_problema(nrlab_nrpb)
        assert(len(note_anumita_problema)==1)
        repo.asignare_nota(nota2)
        assert(len(repo)==2)
        try:
            repo.get_all_note_dupa_id_problema(nrlab_nrpb)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Exista laborator/oare asignat/e si nenotat/e!\n")
        repo.adauga_nota(nota2,nota_pb+1)
        note_anumita_problema2 = repo.get_all_note_dupa_id_problema(nrlab_nrpb)
        assert(len(note_anumita_problema2)==2)
        id_lab_inexistent = nrlab_nrpb + "sarepelimba"
        try:
            repo.get_all_note_dupa_id_problema(id_lab_inexistent)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Aceasta problema nu a fost asignata niciunui student SAU a fost asignata si nu notata!\n")
        
    
    
    def __test_get_all_note_dupa_id_problema_repo_lab_nenotat(self, repo, nrlab_nrpb3, nota3):
        repo.asignare_nota(nota3)
        assert(len(repo)==3)
        try:
            repo.get_all_note_dupa_id_problema(nrlab_nrpb3)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Aceasta problema nu a fost asignata niciunui student SAU a fost asignata si nu notata!\n")
    
    
    def __run_repo_raport1_tests(self):
        repo = self.__test_creeaza_repo_nota_vid()
        id_nota = 12
        student = Student(1,"Gabi",213)
        lab = Laborator("1_1","usor",[13,12,2021])
        nota = NoteDTO(id_nota,student.get_id_student(),lab.get_nrlab_nrpb(),0)
        nrlab_nrpb = lab.get_nrlab_nrpb()
        nota_pb = 8
        id_nota2 = 888
        student2 = Student(2,"Titi",2143)
        nota2 = NoteDTO(id_nota2,student2.get_id_student(),lab.get_nrlab_nrpb(),0)
        self.__test_get_all_note_dupa_id_problema_repo_succes(repo,nrlab_nrpb, nota,nota2,nota_pb)
        id_nota3 = 342
        student3 = Student(3,"Firuta",23)
        lab3 = Laborator("1_33","minune",[8,8,2022])
        nota3 = NoteDTO(id_nota3,student3.get_id_student(),lab3.get_nrlab_nrpb(),0)
        nrlab_nrpb3 = lab3.get_nrlab_nrpb()
        self.__test_get_all_note_dupa_id_problema_repo_lab_nenotat(repo,nrlab_nrpb3, nota3)
    
    
    def __test_raport_note_studenti_lab_dat_ordonati_alfabetic_srv_succes(self, srv, srv_student, srv_lab, id_nota, nota_pb):
        '''
        #functie care returneaza note pentru un laborator, ordonate dupa numele studentilor
        #input: nrlab_nrpb - id lab
        
        - voi da doua note la acelasi lab pentru doi studenti si vreau sa imi afiseze alfabetic
        '''
        srv_student.adauga_student(1,"Tudor",213)
        srv_lab.adauga_lab("1_1","ceva",[12,12,2021])
        srv.asignare_nota(id_nota,1,"1_1")
        srv.adauga_nota(id_nota,nota_pb)

        srv_student.adauga_student(2,"Ana",211)
        srv.asignare_nota(id_nota+1,2,"1_1")
        srv.adauga_nota(id_nota+1,nota_pb+0.3)
        
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==2)
        alfabetic = srv.raport_studenti_cu_note_alfabetic("1_1")
        #assert(str(alfabetic[0]) == "Nota cu id: 2 -> Student:  Ana -> problema: 1_1 -> nota: 9.8.")
        #assert(str(alfabetic[1]) == "Nota cu id: 1 -> Student:  Tudor -> problema: 1_1 -> nota: 9.5.")
        assert(str(alfabetic[0]) == "Ana are nota: 9.8.")
        assert(str(alfabetic[1]) == "Tudor are nota: 9.5.")
    
    
    def __test_raport_note_studenti_lab_dat_ordonati_alfabetic_srv_id_lab_invalid(self,srv):
        try:
            srv.raport_studenti_cu_note_alfabetic("")
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id lab invalid!\n")
        try:
            srv.raport_studenti_cu_note_alfabetic("1")
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id lab invalid!\n")
    
    
    def __test_raport_note_studenti_lab_dat_ordonati_alfabetic_srv_id_lab_inexistent(self, srv):
        try:
            srv.raport_studenti_cu_note_alfabetic("219_2")
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Aceasta problema nu a fost asignata niciunui student SAU a fost asignata si nu notata!\n")
    
    
    def __test_raport_note_studenti_lab_dat_ordonati_dupa_nota_srv_succes(self, srv,srv_student,id_nota,nota_pb):
        '''
        #functie care returneaza note pentru un laborator, ordonate descrescator dupa nota
        #input: nrlab_nrpb - id lab
        
        - voi da trei note la acelasi lab pentru trei studenti si vreau sa imi afiseze ordonat dupa nota
        '''
        srv_student.adauga_student(3,"Gabi",211)
        srv.asignare_nota(id_nota+3,3,"1_1")
        srv.adauga_nota(id_nota+3,nota_pb+0.4)
        all_el = srv.get_all_note_sir()
        assert(len(all_el)==3)
        descrescator_dupa_nota = srv.raport_studenti_cu_note_dupa_nota("1_1")
        assert(len(descrescator_dupa_nota)==3)
        #assert(str(descrescator_dupa_nota[0]) == "Nota cu id: 4 -> Student:  Gabi -> problema: 1_1 -> nota: 9.9.")
        #assert(str(descrescator_dupa_nota[1]) == "Nota cu id: 2 -> Student:  Ana -> problema: 1_1 -> nota: 9.8.")
        #assert(str(descrescator_dupa_nota[2]) == "Nota cu id: 1 -> Student:  Tudor -> problema: 1_1 -> nota: 9.5.")
        assert(str(descrescator_dupa_nota[0]) == "Gabi are nota: 9.9.")
        assert(str(descrescator_dupa_nota[1]) == "Ana are nota: 9.8.")
        assert(str(descrescator_dupa_nota[2]) == "Tudor are nota: 9.5.")
    
    
    def __test_raport_note_studenti_lab_dat_ordonati_dupa_nota_srv_id_lab_invalid(self, srv):
        try:
            srv.raport_studenti_cu_note_dupa_nota("")
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id lab invalid!\n")
        try:
            srv.raport_studenti_cu_note_dupa_nota("42")
            assert(False)
        except ValidationError as ve:
            assert(str(ve)=="Id lab invalid!\n")
    
    
    def __test_raport_note_studenti_lab_dat_ordonati_dupa_nota_srv_id_lab_inexistent(self, srv):
        try:
            srv.raport_studenti_cu_note_dupa_nota("219_2")
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Aceasta problema nu a fost asignata niciunui student SAU a fost asignata si nu notata!\n")
    
    
    def __run_srv_raport1_tests(self):
        repo_note = RepoNote()
        valid_note = ValidatorNote()
        repo_student = RepoStudent()
        repo_lab = RepoLaborator()
        srv = ServiceNote(valid_note,repo_note,repo_student,repo_lab)
        valid_student = ValidatorStudent()
        valid_lab = ValidatorLaborator()
        srv_student = ServiceStudent(valid_student,repo_student)
        srv_lab = ServiceLaborator(valid_lab,repo_lab)
        id_nota = 1
        nota_pb = 9.5
        self.__test_raport_note_studenti_lab_dat_ordonati_alfabetic_srv_succes(srv,srv_student,srv_lab,id_nota,nota_pb)
        self.__test_raport_note_studenti_lab_dat_ordonati_alfabetic_srv_id_lab_invalid(srv)
        self.__test_raport_note_studenti_lab_dat_ordonati_alfabetic_srv_id_lab_inexistent(srv)
        self.__test_raport_note_studenti_lab_dat_ordonati_dupa_nota_srv_succes(srv,srv_student,id_nota,nota_pb)
        self.__test_raport_note_studenti_lab_dat_ordonati_dupa_nota_srv_id_lab_invalid(srv)
        self.__test_raport_note_studenti_lab_dat_ordonati_dupa_nota_srv_id_lab_inexistent(srv)
    
    
    def __test_get_all_pt_raport2_repo_succes(self, repo, nrlab_nrpb, nota, nota2, nota_pb):
        repo.asignare_nota(nota)
        assert(len(repo)==1)
        repo.adauga_nota(nota,nota_pb)
        get_all_fara_none1 = repo.get_all_pt_raport()
        assert(len(get_all_fara_none1)==1)
        repo.asignare_nota(nota2)
        assert(len(repo)==2)
        try:
            repo.get_all_pt_raport()
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="Exista laborator asignat si nenotat!\n")
        repo.adauga_nota(nota2,nota_pb+1)
        get_all_fara_none2 = repo.get_all_pt_raport()
        assert(len(get_all_fara_none2)==2)
    
    
    def __run_repo_raport2_tests(self):
        repo = self.__test_creeaza_repo_nota_vid()
        id_nota = 12
        student = Student(1,"Gabi",213)
        lab = Laborator("1_1","usor",[13,12,2021])
        nota = NoteDTO(id_nota,student.get_id_student(),lab.get_nrlab_nrpb(),0)
        nrlab_nrpb = lab.get_nrlab_nrpb()
        nota_pb = 8
        id_nota2 = 888
        student2 = Student(2,"Titi",2143)
        lab2 = Laborator("1_2","mediu",[11,12,2021])
        nota2 = NoteDTO(id_nota2,student2.get_id_student(),lab2.get_nrlab_nrpb(),0)
        self.__test_get_all_pt_raport2_repo_succes(repo,nrlab_nrpb, nota,nota2,nota_pb)
    
    
    def __test_raport_medie_mai_mica_decat_cinci_srv_succes(self, srv, srv_student, srv_lab, id_nota, nota_pb):
        srv_student.adauga_student(1,"Tudor",213)
        srv_lab.adauga_lab("1_1","ceva",[12,12,2021])
        srv_lab.adauga_lab("1_2","greu",[29,12,2021])
        srv.asignare_nota(id_nota,1,"1_1")
        srv.asignare_nota(id_nota+1,1,"1_2")
        srv.adauga_nota(id_nota,nota_pb)
        srv.adauga_nota(id_nota+1,nota_pb-0.5)
        medii = srv.raport_studenti_medie_sub_cinci()
        assert(len(medii)==0)
        srv_student.adauga_student(2,"Vlad",214)
        srv.asignare_nota(id_nota+2,2,"1_1")
        srv.asignare_nota(id_nota+3,2,"1_2")
        srv.adauga_nota(id_nota+2,4)
        srv.adauga_nota(id_nota+3,3.5)
        medii2 = srv.raport_studenti_medie_sub_cinci()
        assert(len(medii2)==1)
        assert(str(medii2[0][0])=="Vlad")
        assert(medii2[0][1]==3.75)
        
    
    
    def __run_srv_raport2_tests(self):
        repo_note = RepoNote()
        valid_note = ValidatorNote()
        repo_student = RepoStudent()
        repo_lab = RepoLaborator()
        srv = ServiceNote(valid_note,repo_note,repo_student,repo_lab)
        valid_student = ValidatorStudent()
        valid_lab = ValidatorLaborator()
        srv_student = ServiceStudent(valid_student,repo_student)
        srv_lab = ServiceLaborator(valid_lab,repo_lab)
        id_nota = 1
        nota_pb = 9.5
        self.__test_raport_medie_mai_mica_decat_cinci_srv_succes(srv,srv_student,srv_lab,id_nota,nota_pb)
    
    
    def __test_raport_primele_cincizeci_la_suta_medie_peste_cinci_srv_succes(self, srv, srv_student, srv_lab, id_nota, nota_pb):
        srv_student.adauga_student(1,"Tudor",213)
        srv_lab.adauga_lab("1_3","ceva",[12,12,2021])
        srv_lab.adauga_lab("1_2","greu",[29,12,2021])
        srv.asignare_nota(id_nota,1,"1_3")
        srv.asignare_nota(id_nota+1,1,"1_2")
        srv.adauga_nota(id_nota,nota_pb-0.5) #9
        srv.adauga_nota(id_nota+1,nota_pb-1.5) #8
        srv_student.adauga_student(2,"Vlad",214)
        srv.asignare_nota(id_nota+2,2,"1_3")
        srv.asignare_nota(id_nota+3,2,"1_2")
        srv.adauga_nota(id_nota+2,6)
        srv.adauga_nota(id_nota+3,5)
        rezultat = srv.raport_primele_cincizeci_la_suta_note_lab_medie_peste_cinci()
        assert(len(rezultat)==1)
        assert(str(rezultat[0])=="Laboratorul 1_2 are 2 note, iar media notelor de la acest laborator este: 6.5!")
        srv_student.adauga_student(3,"Mihai",214)
        srv_lab.adauga_lab("1_5","ceva",[12,12,2021])
        srv.asignare_nota(id_nota+4,1,"1_5")
        srv.asignare_nota(id_nota+5,2,"1_5")
        srv.adauga_nota(id_nota+4,6)
        srv.adauga_nota(id_nota+5,5)
        rezultat2 = srv.raport_primele_cincizeci_la_suta_note_lab_medie_peste_cinci()
        assert(len(rezultat2)==1)
        srv_lab.adauga_lab("1_10","ceva",[12,12,2021])
        srv.asignare_nota(id_nota+6,1,"1_10")
        srv.asignare_nota(id_nota+7,3,"1_10")
        srv.adauga_nota(id_nota+6,3)
        srv.adauga_nota(id_nota+7,5)
        rezultat3 = srv.raport_primele_cincizeci_la_suta_note_lab_medie_peste_cinci()
        assert(len(rezultat3)==1)
        srv_lab.adauga_lab("1_101","ceva",[12,12,2021])
        srv.asignare_nota(id_nota+8,1,"1_101")
        srv.asignare_nota(id_nota+9,3,"1_101")
        srv.adauga_nota(id_nota+8,7)
        srv.adauga_nota(id_nota+9,5)
        rezultat4 = srv.raport_primele_cincizeci_la_suta_note_lab_medie_peste_cinci()
        assert(len(rezultat4)==2)
        assert(str(rezultat4[0])=="Laboratorul 1_101 are 2 note, iar media notelor de la acest laborator este: 6.0!")
        assert(str(rezultat4[1])=="Laboratorul 1_2 are 2 note, iar media notelor de la acest laborator este: 6.5!")
    
    
    def __run_srv_raport_nou_tests(self):
        repo_note = RepoNote()
        valid_note = ValidatorNote()
        repo_student = RepoStudent()
        repo_lab = RepoLaborator()
        srv = ServiceNote(valid_note,repo_note,repo_student,repo_lab)
        valid_student = ValidatorStudent()
        valid_lab = ValidatorLaborator()
        srv_student = ServiceStudent(valid_student,repo_student)
        srv_lab = ServiceLaborator(valid_lab,repo_lab)
        id_nota = 1
        nota_pb = 9.5
        self.__test_raport_primele_cincizeci_la_suta_medie_peste_cinci_srv_succes(srv,srv_student,srv_lab,id_nota,nota_pb)
    
    
    def __run_test_sortari(self):
        #sortari cu InsertSort
        lista = [5,2,3,6,11,23,9,10,1,4]
        sorter = InsertSort()
        sorter.sort(lista)
        assert(lista == [1,2,3,4,5,6,9,10,11,23])
        lista = [5,2,3,6,11,23,9,10,1,4]
        sorter.sort(lista,reverse=True)
        assert(lista == [23,11,10,9,6,5,4,3,2,1])
        student1 = Student(1,"Fabi",212)
        student2 = Student(2,"Ana",213)
        student3 = Student(3,"Maria",211)
        student4 = Student(4,"Gelu",212)
        lista_studenti = [student1,student2,student3,student4]
        sorter.sort(lista_studenti,key=lambda x:x.get_nume())
        assert(lista_studenti == [student2,student1,student4,student3])
        sorter.sort(lista_studenti,key=lambda x:x.get_nume(),reverse = True)
        assert(lista_studenti == [student3,student4,student1,student2])
        student1 = Student(1,"Fabi",212)
        student2 = Student(2,"Ana",213)
        student3 = Student(3,"Ana",211)
        student4 = Student(4,"Gelu",212)
        student5 = Student(5,"Ana",215)
        student6 = Student(6,"Ana",214)
        
        lista_studenti = [student1,student2,student3,student4]
        sorter.sort(lista_studenti,key=lambda x: (x.get_nume(),x.get_grupa()))
        assert(lista_studenti == [student3,student2,student1,student4])
        sorter.sort(lista_studenti,key=lambda x: (x.get_nume(),x.get_grupa()), cmp = lambda x,y:x>y)
        assert(lista_studenti == [student4,student1,student2,student3])
        
        lista_4_ana = [student1,student2,student3,student4,student5,student6]
        sorter.sort(lista_4_ana,key=lambda x: (x.get_nume(),x.get_grupa()))
        assert(lista_4_ana == [student3,student2,student6,student5,student1,student4])
        
        
        
        sorter.sort(lista_4_ana,key=lambda x: (x.get_nume(),x.get_grupa()), cmp = lambda x,y:x>y)
        assert(lista_4_ana == [student4,student1,student5,student6,student2,student3])
        
        sorter.sort(lista_4_ana,key=lambda x: (x.get_nume(),x.get_grupa()), cmp = lambda x,y:x>y, reverse = True)
        assert(lista_4_ana == [student3,student2,student6,student5,student1,student4])
        #sortari cu CombSort
        lista2 = [5,2,3,6,11,23,9,10,1,4]
        sorter2 = CombSort()
        sorter2.sort(lista2)
        assert(lista2 == [1,2,3,4,5,6,9,10,11,23])
        lista2 = [5,2,3,6,11,23,9,10,1,4]
        sorter2.sort(lista2,reverse=True) 
        assert(lista2 == [23,11,10,9,6,5,4,3,2,1])
        student1 = Student(1,"Fabi",212)
        student2 = Student(2,"Ana",213)
        student3 = Student(3,"Maria",211)
        student4 = Student(4,"Gelu",212)
        lista_studenti2 = [student1,student2,student3,student4]
        sorter2.sort(lista_studenti2,key=lambda x:x.get_nume())
        assert(lista_studenti2 == [student2,student1,student4,student3])
        sorter2.sort(lista_studenti2,key=lambda x:x.get_nume(),reverse = True)
        assert(lista_studenti2 == [student3,student4,student1,student2])
        #sortari cu mergesort
        lista3 = [5,2,3,6,11,23,9,10,1,4]
        sorter3 = MergeSort()
        sorter3.sort(lista3)
        assert(lista3 == [1,2,3,4,5,6,9,10,11,23])
        student1 = Student(1,"Fabi",212)
        student2 = Student(2,"Ana",213)
        student3 = Student(3,"Maria",211)
        student4 = Student(4,"Gelu",212)
        lista_studenti2 = [student1,student2,student3,student4]
        sorter3.sort(lista_studenti2,key=lambda x:x.get_nume())
        assert(lista_studenti2 == [student2,student1,student4,student3])
        sorter3.sort(lista_studenti2,key=lambda x:x.get_nume(),reverse = True)
        assert(lista_studenti2 == [student3,student4,student1,student2])
    
    
    def run_all_tests(self):
        #print("start teste...")
        #pentru student
        self.__run_creeaza_student_test()
        self.__run_valideaza_student_tests()
        self.__run_repo_adauga_student_tests()
        self.__run_srv_adauga_student_tests()
        self.__run_repo_sterge_student_tests()
        self.__run_srv_sterge_student_tests()
        self.__run_repo_modifica_nume_student_tests()
        self.__run_srv_modifica_nume_student_tests()
        self.__run_repo_modifica_grupa_student_tests()
        self.__run_srv_modifica_grupa_student_tests()
        self.__run_repo_cauta_student_tests()
        self.__run_srv_cauta_student_tests()
        #pentru laborator
        self.__run_creeaza_lab_test()
        self.__run_valideaza_lab_tests()
        self.__run_repo_adauga_lab_tests()
        self.__run_srv_adauga_lab_tests()
        self.__run_repo_sterge_lab_tests()
        self.__run_srv_sterge_lab_tests()
        self.__run_repo_modifica_descriere_lab_tests()
        self.__run_srv_modifica_descriere_lab_tests()
        self.__run_repo_modifica_deadline_lab_tests()
        self.__run_srv_modifica_deadline_lab_tests()
        self.__run_repo_cauta_lab_tests()
        self.__run_srv_cauta_lab_tests()
        self.__run_repo_filtreaza_dupa_prefix_tests()
        self.__run_srv_filtreaza_dupa_prefix_tests()
        #pentru note + rapoarte
        self.__run_creeaza_nota_test()
        self.__run_valideaza_nota_tests()
        self.__run_repo_asignare_nota_tests()
        self.__run_srv_asignare_nota_tests()
        self.__run_repo_adaugare_nota_tests()
        self.__run_srv_adaugare_nota_tests()
        self.__run_repo_sterge_nota_tests()
        self.__run_srv_sterge_nota_tests()
        self.__run_repo_raport1_tests()
        self.__run_srv_raport1_tests()
        self.__run_repo_raport2_tests()
        self.__run_srv_raport2_tests()
        self.__run_srv_raport_nou_tests()
        #............
        self.__run_test_sortari()
        #print("end teste...")
