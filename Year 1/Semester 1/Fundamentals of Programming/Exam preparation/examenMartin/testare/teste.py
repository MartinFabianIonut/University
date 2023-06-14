from domain.entitati import Eveniment
from erors.exceptii import ValidError, RepoError
from validation.validatori import ValidatorEveniment
from repository.repozitorii import FileRepoEvenimente
from controller.servicii import ServiceEvenimente
class Teste(object):
    
    
    def __test_creeaza_succes(self, ide, data, ora, descriere):
        eveniment = Eveniment(ide,data,ora,descriere)
        assert eveniment.get_ide()==ide
        assert eveniment.get_data() == data
        assert eveniment.get_ora() == ora
        assert eveniment.get_descriere() == descriere
        return eveniment
    
    
    def __test_acelasi_eveniment(self, eveniment, ide, data, ora, descriere):
        eveniment2 = Eveniment(ide,data,ora,descriere)
        assert eveniment == eveniment2
    
    
    def __test_eveniment_data_invalida(self, ide, datar, ora, descriere):
        try:
            ev = Eveniment(ide,datar,ora,descriere)
            valid = ValidatorEveniment()
            valid.valideaza(ev)
            assert (False)
        except ValidError as ve:
            assert str(ve) == "Data invalida!\n"
    
    
    def __test_eveniment_zi_luna_invalida(self, ide, datar, ora, descriere):
        try:
            ev = Eveniment(ide,datar,ora,descriere)
            valid = ValidatorEveniment()
            valid.valideaza(ev)
            assert (False)
        except ValidError as ve:
            assert str(ve) == "Data invalida!\n"
    
    
    def __run_test_creeaza_eveniment(self):
        ide = 1
        data = "23.04.2024"
        ora = "14.50"
        descriere = "Ceva sublim"
        eveniment = self.__test_creeaza_succes(ide,data,ora,descriere)
        self.__test_acelasi_eveniment(eveniment,ide,data,ora,descriere)
        datar = "45.10.2021"
        self.__test_eveniment_data_invalida(ide,datar,ora,descriere)
        datar="32.-1.2022"
        self.__test_eveniment_zi_luna_invalida(ide,datar,ora,descriere)
    
    
    def __run_test_vezi_evenimente(self):
        #pentru get
        valid = ValidatorEveniment()
        repo = FileRepoEvenimente("testare/test.txt")
        srv = ServiceEvenimente(valid,repo)
        evenimente = srv.get_all()
        assert len(evenimente)==3
        assert evenimente[0].get_ide()==3
        assert evenimente[0].get_data() == "14.10.2023"
        assert evenimente[0].get_ora() == "18.20"
        assert evenimente[0].get_descriere() == "Cred ca o sa prinda bine"
    
    
    def __run_adauga_repo(self):
        repo = FileRepoEvenimente("testare/test.txt")
        evens = repo.get_all()
        assert len(evens)==3
        eveniment_nou = Eveniment(14,"19.02.2024","19.00","Minunat")
        repo.adauga_ev(eveniment_nou)
        evens2 = repo.get_all()
        assert len(evens2)==4
        repo.exporteaza(evens, "testare/test.txt")
        evens3 = repo.get_all()
        assert len(evens3)==3
        
    def __run_adauga_srv(self):
        valid = ValidatorEveniment()
        repo = FileRepoEvenimente("testare/test.txt")
        srv = ServiceEvenimente(valid,repo)
        ide = 222
        data = "23.04.2024"
        ora = "14.50"
        descriere = "Ceva sublim"
        evens = srv.get_all()
        assert len(evens)==3
        srv.adauga(ide, data, ora, descriere)
        evens2 = srv.get_all()
        assert len(evens2)==4
        repo.exporteaza(evens, "testare/test.txt")
        evens3 = repo.get_all()
        assert len(evens3)==3
    
    
    def __run_ziua_curenta(self):
        valid = ValidatorEveniment()
        repo = FileRepoEvenimente("testare/test.txt")
        srv = ServiceEvenimente(valid,repo)
        evens = srv.ziua_curenta()
        assert len(evens)==2
        assert evens[0].get_ide() == 4
    
    
    def __run_ziua_data_de_utilizator(self):
        valid = ValidatorEveniment()
        repo = FileRepoEvenimente("testare/test.txt")
        srv = ServiceEvenimente(valid,repo)
        datan = "18.10.2021"
        try:
            srv.filtrare(datan)
            assert False
        except RepoError as re:
            assert str(re)=="Nu exista asemenea date!\n"
        datab = "14.10.2023"
        evens = srv.filtrare(datab)
        assert len(evens)==1
    
    
    def __run_exporta(self):
        #prin acesta se testeaza si write_all
        valid = ValidatorEveniment()
        repo = FileRepoEvenimente("testare/test.txt")
        srv = ServiceEvenimente(valid,repo) 
        nume = "testare/adaugtest.txt"
        sir = "re"
        srv.export(nume, sir)
        valid = ValidatorEveniment()
        repo = FileRepoEvenimente("testare/adaugtest.txt")
        srv = ServiceEvenimente(valid,repo)
        evenimente = srv.get_all()
        assert len(evenimente)==2
    
    
    def run_all_tests(self):
        self.__run_test_creeaza_eveniment()
        self.__run_test_vezi_evenimente()
        self.__run_ziua_curenta()
        self.__run_ziua_data_de_utilizator()
        self.__run_adauga_repo()
        self.__run_adauga_srv()
        self.__run_exporta()
        
    
    



