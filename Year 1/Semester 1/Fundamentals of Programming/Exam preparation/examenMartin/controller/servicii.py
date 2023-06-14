from domain.entitati import Eveniment
from _datetime import date
from erors.exceptii import RepoError
class ServiceEvenimente(object):
    
    
    def __init__(self, valid, repo):
        self.__valid = valid
        self.__repo = repo
    
    
    def get_all(self):
        #functie care returneaza toate evenimentele
        #output: lista cu acele evenimente
        return self.__repo.get_all()
    
    def ziua_curenta(self):
        #functie care returneaza evenimentele din data de azi, 
        #ordonate crescator dupa ora
        #output: lista cu acele evenimente, daca exista
        #exceptie: nu sunt evenimente azi => RepoError
        d = date.today()
        data = str(d)
        parti = data.split("-")
        an = parti[0]
        luna= parti[1]
        zi = parti[2]
        evenimente = self.get_all()
        de_afisat = []
        for eveniment in evenimente:
            data = eveniment.get_data()
            parti2 = data.split(".")
            if len(parti2)==3:
                if parti2[0]==zi and (parti2[1]==luna or parti2[1]=="1") and parti2[2]==an:
                    de_afisat.append(eveniment)
        de_afisat.sort(key=lambda x:x.get_ora())
        return de_afisat
        
    
    def adauga(self,ide,data,ora,descriere):
        #functie care adauga un eveniment in fisier
        #input: ide - int
        #       data
        #       ora
        #       descriere - ultimele 3 str
        #rezultatul se vede in fisier imediat
        #exceptii: elemenetele care compun evenimentul nu sunt valide => ValidError
        #          exista deja evenimentul in fisier => RepoError
        eveniment = Eveniment(ide,data,ora,descriere)
        self.__valid.valideaza(eveniment)
        self.__repo.adauga_ev(eveniment)
        
    def __ajutor(self,zi,luna,an):
        #functie anexa celei de filtrare
        #input: zi,luna,an -int
        #output: lista ordonata descrescator
        evenimente = self.get_all()
        de_afisat = []
        for eveniment in evenimente:
            data = eveniment.get_data()
            parti2 = data.split(".")
            if len(parti2)==3:
                if parti2[0]==zi and (parti2[1]==luna or parti2[1]=="1") and parti2[2]==an:
                    de_afisat.append(eveniment)
        de_afisat.sort(key=lambda x:x.get_ora())
        if len(de_afisat)>0:
            return de_afisat
        raise RepoError("Nu exista asemenea date!\n")
        
    def filtrare(self,data):
        #functie care returneaza evenimentele dintr-o data introdusa de utilizator, 
        #ordonate crescator dupa ora
        #output: lista cu acele evenimente, daca exista
        #exceptie: nu sunt evenimente in data respectiva => RepoError
        self.__valid.valideaza_data(data)
        evenimente = self.get_all()
        filtru = []
        for even in evenimente:
            if data == even.get_data():
                filtru.append(even)
        if len(filtru)>0:
            parti = data.split(".")
            ziD = parti[0]
            lunaD = parti[1]
            anD = parti[2]
            return self.__ajutor(ziD,lunaD,anD)
        raise RepoError("Nu exista asemenea date!\n")
        
        
    def export(self,nume,sir):
        #functie care exporteaza evenimente din fisierul meu daca un sir dat de la tastatura
        #se gaseste in descrierea unui eveniment
        #exceptie: nu exista asemenea evenimente => RepoError
        evenimente = self.get_all()
        de_adaugat=[]
        for eveniment in evenimente:
            descriere = eveniment.get_descriere()
            if descriere.find(sir) != -1:
                de_adaugat.append(eveniment)
        if len(de_adaugat)>0:
            self.__repo.exporteaza(de_adaugat,nume)
        else:
            raise RepoError("Nu exista nimic de exportat!\n")

