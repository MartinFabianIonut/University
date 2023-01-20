from repository.repozitorii import FileRepoCarte, FileRepoImprumut
from validation.validatori import ValidatorCarte, ValidatorImprumut
from controller.servicii import ServiceCarti, ServiceImprumut
from erors.exceptii import RepositoryError
from domain.entitati import Carte


class Teste(object):
    
    
    def __test_creeaza_carte_succes(self, id_carte, nume_carte, autor, an):
        carte = Carte(id_carte,nume_carte,autor,an) 
        assert(carte.get_id_carte()==id_carte)
        assert(carte.get_nume_carte()==nume_carte)
        assert(carte.get_autor() == autor)
        assert(carte.get_an()==an)
        carte.set_nume_carte("Nume nou")
        carte.set_autor("Autor nou")
        carte.set_an(2000)
        assert(carte.get_nume_carte()=="Nume nou")
        assert(carte.get_autor() == "Autor nou")
        assert(carte.get_an()==2000)
        return carte
    
    
    def __test_egalitate_carti(self, carte):
        alta_carte_acelasi_id = Carte(carte.get_id_carte(),"Nume", "Autor",2020) 
        assert(carte == alta_carte_acelasi_id)
    
    
    def __test_printare_carte(self, carte):
        assert(str(carte)=="Id: 1, Titlu: Nume nou, Autor: Autor nou, Anul: 2000.")
    
    
    def __run_creeaza_carte(self):
        id_carte = 1
        nume_carte = "Mizerabilii"
        autor = "Victor Hugo"
        an=1820
        carte = self.__test_creeaza_carte_succes(id_carte,nume_carte,autor,an)
        self.__test_egalitate_carti(carte)
        self.__test_printare_carte(carte)
    
    def __test_repo_carti(self):
        file_path = "testare/test_carti.txt"
        repo_carti = FileRepoCarte(file_path)
        assert(len(repo_carti)==6)
        carti = repo_carti.get_all()
        #1,Zori la Semineu,Petre Ispirescu,1990
        assert(carti[0].get_id_carte()==1)
        assert(carti[0].get_nume_carte()=="Zori la Semineu")
        assert(carti[0].get_autor()=="Petre Ispirescu")
        assert(carti[0].get_an()==1990)
        
    
    def __test_srv_se_termina_cu(self):
        file_path = "testare/test_carti.txt"
        repo_carti = FileRepoCarte(file_path)
        valid_carti = ValidatorCarte()
        srv_carti = ServiceCarti(valid_carti,repo_carti)
        termina = "eu"
        se_termina_cu_termina = srv_carti.se_termina_cu(termina)
        assert(len(se_termina_cu_termina)==2)
        #1,Zori la Semineu,Petre Ispirescu,1990
        assert(se_termina_cu_termina[0].get_id_carte()==1)
        assert(se_termina_cu_termina[0].get_nume_carte()=="Zori la Semineu")
        assert(se_termina_cu_termina[0].get_autor()=="Petre Ispirescu")
        assert(se_termina_cu_termina[0].get_an()==1990)
        #2,Catelul meu,Claudiu Costea,2021
        assert(se_termina_cu_termina[1].get_id_carte()==2)
        assert(se_termina_cu_termina[1].get_nume_carte()=="Catelul meu")
        assert(se_termina_cu_termina[1].get_autor()=="Claudiu Costea")
        assert(se_termina_cu_termina[1].get_an()==2021)
        nu_termina = "dar"
        try:
            srv_carti.se_termina_cu(nu_termina)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="\nNu exista carti care sa se termine cu sirul citit!\n")
    
    
    def __test_repo_imprumuturi(self):
        file_path = "testare/test_imprumuturi.txt" 
        repo_imprumut = FileRepoImprumut(file_path)
        assert(len(repo_imprumut)==4)
        imprumuturi = repo_imprumut.get_all()
        #1,1,18.03.2020,3
        assert(imprumuturi[0].get_id_imprumut()==1)
        assert(imprumuturi[0].get_id_carte()==1)
        assert(imprumuturi[0].get_data_imprumut()=="18.03.2020")
        assert(imprumuturi[0].get_durata_imprumut()==3)
    
    
    def __test_srv_durata_x(self):
        file_path_c = "testare/test_carti.txt"
        file_path_i = "testare/test_imprumuturi.txt" 
        repo_carti = FileRepoCarte(file_path_c)
        repo_imprumuturi = FileRepoImprumut(file_path_i)
        valid_imprumut = ValidatorImprumut()
        srv_imprumuturi = ServiceImprumut(valid_imprumut,repo_imprumuturi,repo_carti)
        durata = 14
        imprumuturi = srv_imprumuturi.durata_x(durata)
        assert(len(imprumuturi)==1)
        #2,4,03.12.2020,14
        assert(imprumuturi[0].get_id_imprumut() == 2)
        assert(imprumuturi[0].get_id_carte() == 4)
        assert(imprumuturi[0].get_data_imprumut() == "03.12.2020")
        assert(imprumuturi[0].get_durata_imprumut() == 14)
        nu_durata = 20
        try:
            srv_imprumuturi.durata_x(nu_durata)
            assert(False)
        except RepositoryError as re:
            assert(str(re)=="\nNu exista imprumuturi care sa dureze cat ati introdus!\n")
    
    
    
    
    
    def run_all_tests(self):
        self.__run_creeaza_carte()
        self.__test_repo_carti()
        self.__test_srv_se_termina_cu()
        self.__test_repo_imprumuturi()
        self.__test_srv_durata_x()
    
    



