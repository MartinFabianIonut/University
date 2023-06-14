from erors.exceptii import RepositoryError

class ServiceCarti(object):
    
    
    def __init__(self, valid_carte, repo_carte):
        self.__valid_carte = valid_carte
        self.__repo_carte = repo_carte
        
    def se_termina_cu(self,sir):
        #functie care returneaza un sir de carti care au la finalul titlului lor un sir de caractere citit de la tastarura
        #input: sir - string
        #outpur: de_afisat - lista cu carti
        #exceptii: daca nu s-au gasit carti corespunsatoare, se ridica eroarea de tipul RepositoryError, care semnaleaza ca nu exista
        carti = self.__repo_carte.get_all()
        de_afisat = []
        for carte in carti:
            nume_carte = carte.get_nume_carte()
            sfarsit = len(nume_carte)
            inceput = len(nume_carte)-len(sir)
            if nume_carte.find(sir,inceput,sfarsit) != -1: #aici caut daca e la finalul titlului
                de_afisat.append(carte)
        if len(de_afisat)>0:
            de_afisat.sort(key = lambda an: an.get_an()) #aici sortez dupa an, crescator
            return de_afisat
        raise RepositoryError("\nNu exista carti care sa se termine cu sirul citit!\n")
    
    



class ServiceImprumut(object):
    
    
    def __init__(self, valid_imprumut, repo_imprumut, repo_carte):
        self.__valid_imprumut = valid_imprumut
        self.__repo_imprumut = repo_imprumut
        self.__repo_carte = repo_carte
    
    def durata_x(self,durata):
        #functie care returneaza un sir de imprumuturi care au durata egala cu cea citita de la tastatura
        #input: durata - nr int
        #outpur: de_afisat - lista cu imprumuturi
        #exceptii: daca nu s-au gasit carti corespunsatoare, se ridica eroarea de tipul RepositoryError, care semnaleaza ca nu exista
        imprumuturi = self.__repo_imprumut.get_all()
        de_afisat = []
        for imprumut in imprumuturi:
            if imprumut.get_durata_imprumut() == durata:
                de_afisat.append(imprumut)
        if len(de_afisat)>0:
            return de_afisat
        raise RepositoryError("\nNu exista imprumuturi care sa dureze cat ati introdus!\n")
    



