from domain.entitati import ObjDTO
from erori.exceptii import RepoError

class ServiceConcurenti(object):
    
    
    def __init__(self, valid_concurent, repo_concurenti):
        self.__valid_concurent = valid_concurent
        self.__repo_concurenti = repo_concurenti
    
    def nascuti_dupa_an(self, an):
        concurentii = self.__repo_concurenti.get_all()
        de_afisat = []
        for concurent in concurentii:
            data_nasterii_concurent_curent = concurent.get_data_nasterii()
            data_nasterii = data_nasterii_concurent_curent.split(".")
            anul = int(data_nasterii[2])
            if anul > an:
                de_afisat.append(concurent)
        if len(de_afisat)>0:
            return de_afisat
        raise RepoError("\nNu exista concurenti nascuti dupa anul introdus!\n")
                


class ServiceParticipari(object):
    
    
    def __init__(self, valid_participare, repo_participari, repo_concurenti):
        self.__valid_participare = valid_participare
        self.__repo_participari = repo_participari
        self.__repo_concurenti = repo_concurenti
    
    def clasament(self):
        participari = self.__repo_participari.get_all()
        punctaje_pe_tara = {}
        for participare in participari:
            id_concurent = participare.get_id_concurent()
            concurent = self.__repo_concurenti.cauta_concurent(id_concurent)
            tara = concurent.get_tara()
            if tara not in punctaje_pe_tara:
                punctaje_pe_tara[tara] = 0
            punctaje_pe_tara[tara] += participare.get_punctaj()
        rezultat = []
        for punctaje_de_afisat in punctaje_pe_tara:
            tara = punctaje_de_afisat
            punctaj_total = punctaje_pe_tara[punctaje_de_afisat]
            obiect_dto = ObjDTO(tara,punctaj_total)
            rezultat.append(obiect_dto)
        if len(rezultat)>0:
            rezultat.sort(key=lambda cel_mai_mare_punctaj: cel_mai_mare_punctaj.get_punctaj_total(), reverse=True)
        return rezultat



