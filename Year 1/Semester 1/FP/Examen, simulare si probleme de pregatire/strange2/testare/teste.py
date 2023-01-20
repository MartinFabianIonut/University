from repository.repozitorii import FileRepoConcurenti, FileRepoParticipari
from validation.validatori import ValidatorConcurenti, ValidatorParticipari
from controller.servicii import ServiceConcurenti, ServiceParticipari
from erori.exceptii import RepoError
from domain.entitati import Concurent

class Teste(object):
    
    
    def __test_creeaza_concurent(self,id_concurent,nume,tara,data_nasterii):
        concurent = Concurent(id_concurent,nume,tara,data_nasterii)
        assert(concurent.get_id_concurent()==id_concurent)
        assert(concurent.get_nume()==nume)
        assert(concurent.get_tara()==tara)
        assert(concurent.get_data_nasterii()==data_nasterii)
        concurent.set_nume("Vlad")
        concurent.set_tara("Finlanda")
        concurent.set_data_nasterii("13.05.1990")
        assert(concurent.get_id_concurent()==1)
        assert(concurent.get_nume()=="Vlad")
        assert(concurent.get_tara()=="Finlanda")
        assert(concurent.get_data_nasterii()=="13.05.1990")
        return concurent
    

    def __test_egalitate_concurenti(self, concurent, id_concurent, alt_nume):
        alt_concurent_acelasi_id = Concurent(id_concurent,alt_nume,concurent.get_tara(),concurent.get_data_nasterii())
        assert(alt_concurent_acelasi_id == concurent)
    
    
    def __test_printare_concurent(self, concurent):
        assert(str(concurent)=="Id: 1, Nume: Vlad, Tara: Finlanda, Data nasterii: 13.05.1990")
    
    
    def __run_test_creeaza_concurent(self):
        id_concurent = 1
        nume = "Gabi"
        tara = "Romania"
        data_nasterii = "12.04.1989"
        concurent = self.__test_creeaza_concurent(id_concurent,nume,tara,data_nasterii)
        alt_nume = "Emanuel"
        self.__test_egalitate_concurenti(concurent,id_concurent,alt_nume)
        self.__test_printare_concurent(concurent)
        
    
    def __test_repo_concurenti(self):
        file_path = "testare/test_concurenti.txt"
        repo = FileRepoConcurenti(file_path)
        assert(len(repo)==5)
        concurenti = repo.get_all()
        assert(concurenti[0].get_id_concurent()==1)
        assert(concurenti[0].get_nume()=="Dan")
        assert(concurenti[0].get_tara()=="Franta")
        assert(concurenti[0].get_data_nasterii()=="12.10.2010")
            
    def __test_dupa_an(self):
        file_path = "testare/test_concurenti.txt"
        repo = FileRepoConcurenti(file_path)
        valid = ValidatorConcurenti()
        srv = ServiceConcurenti(valid,repo)
        an = 2000
        cei_nascuti_dupa_un_an_dat = srv.nascuti_dupa_an(an)
        assert(len(cei_nascuti_dupa_un_an_dat)==3)
        #1,Dan,Franta,12.10.2010
        assert(cei_nascuti_dupa_un_an_dat[0].get_id_concurent()==1)
        assert(cei_nascuti_dupa_un_an_dat[0].get_nume()=="Dan")
        assert(cei_nascuti_dupa_un_an_dat[0].get_tara()=="Franta")
        assert(cei_nascuti_dupa_un_an_dat[0].get_data_nasterii()=="12.10.2010")
        #2,Luca,Spania,03.02.2002
        assert(cei_nascuti_dupa_un_an_dat[1].get_id_concurent()==2)
        assert(cei_nascuti_dupa_un_an_dat[1].get_nume()=="Luca")
        assert(cei_nascuti_dupa_un_an_dat[1].get_tara()=="Spania")
        assert(cei_nascuti_dupa_un_an_dat[1].get_data_nasterii()=="03.02.2002")
        #5,Denis,Turcia,22.08.2003
        assert(cei_nascuti_dupa_un_an_dat[2].get_id_concurent()==5)
        assert(cei_nascuti_dupa_un_an_dat[2].get_nume()=="Denis")
        assert(cei_nascuti_dupa_un_an_dat[2].get_tara()=="Turcia")
        assert(cei_nascuti_dupa_un_an_dat[2].get_data_nasterii()=="22.08.2003")
    
    
    def __test_repo_cauta_concurent_dupa_id(self):
        file_path = "testare/test_concurenti.txt"
        repo = FileRepoConcurenti(file_path)
        concurent_cautat_si_gasit_cu_succes = repo.cauta_concurent(1)
        assert(concurent_cautat_si_gasit_cu_succes.get_nume()=="Dan")
        try:
            repo.cauta_concurent(7)
            assert(False)
        except RepoError as re:
            assert(str(re)=="\nNu exsita concurent cu asemenea id!\n")
    
    
    def __test_repo_participari(self):
        file_path = "testare/test_participari.txt"
        repo = FileRepoParticipari(file_path)
        assert(len(repo)==4)
        participari = repo.get_all()
        assert(participari[0].get_cod()==42)
        assert(participari[0].get_id_concurent()==1)
        assert(abs(participari[0].get_punctaj()-78)<0.000001)
    
    
    def __test_clasament(self):
        file_path = "testare/test_participari.txt" 
        repo = FileRepoParticipari(file_path)
        repo_concurenti = FileRepoConcurenti("testare/test_concurenti.txt")
        valid = ValidatorParticipari()
        srv = ServiceParticipari(valid, repo, repo_concurenti)
        top = srv.clasament()
        assert(len(top)==3)
        #Franta,166
        assert(top[0].get_tara()=="Franta")
        assert(top[0].get_punctaj_total()==166)
        assert(str(top[0])=="Tara Franta are punctajul total: 166.0.")
        
    
    
    
    
    def run_all_tests(self):
        self.__run_test_creeaza_concurent()
        self.__test_repo_concurenti()
        self.__test_repo_cauta_concurent_dupa_id()
        self.__test_dupa_an()
        self.__test_repo_participari()
        self.__test_clasament()
    
    



