from domain.entitati import Piesa
import random
import string

class ServicePiesa(object):
    
    
    def __init__(self, val, repo):
        self.__val = val
        self.__repo = repo
    
    def adauga_piesa(self,titlu,regizor,gen,durata):
        piesa = Piesa(titlu,regizor,gen,durata)
        self.__val.valideaza(piesa)
        self.__repo.adauga_piesa(piesa)
        
    def get_all(self):
        return self.__repo.get_all()
    
    def genereaza(self,cate):
        for i in range(cate):
            titlu = ""
            regizor = ""
            gen = ""
            durata = 0
            lg = random.randint(8,12)
            litere = string.ascii_lowercase
            vocale = "aeiou"
            rez = ''.join(random.choice(litere) for k in range(lg))
            '''
            for j in range(1,lg):
                print(rez)
                if rez[j-1].find(vocale)!=-1:
                    l = random.choice(litere)
                    while l.find(vocale)!=-1:
                        l = random.choice(litere)
                    rez += l 
                if rez[j-1].find(vocale)==-1:
                    l = random.choice(litere)
                    while l.find(vocale)==-1:
                        l = random.choice(litere)
                    rez += l 
                    '''
            print(rez)
            titlu = rez
            regizor = ''.join(random.choice(litere) for k in range(lg))
            genuri = ["Drama","Comedie","Satira","Altele"]
            gen = random.choice(genuri)
            durata = random.randint(1,10)
            self.adauga_piesa(titlu, regizor, gen, durata)

    def exporta(self,nume):
        self.__repo.exp(nume)

