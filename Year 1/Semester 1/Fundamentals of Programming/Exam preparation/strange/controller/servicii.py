from domain.entitati import PictDTO
class ServicePicturi(object):
    
    
    def __init__(self, valid_pictura, repo_picturi):
        self.__valid_pictura = valid_pictura
        self.__repo_picturi = repo_picturi
    
    def get_all_picturi(self):
        return self.__repo_picturi.get_all()
    
    def unu(self,sir):
        toate_picturile = self.__repo_picturi.get_all()
        picturi_de_af = []
        for pictura in toate_picturile:
            if pictura.get_nume().find(sir) != -1:
                picturi_de_af.append(pictura)
        if len(picturi_de_af)>0:
            picturi_de_af.sort(key = lambda an: an.get_an(), reverse = True)
            return picturi_de_af
        return picturi_de_af

    def doi(self):
        toate_picturile = self.__repo_picturi.get_all()
        #picturi_de_af = []
        autori = {}
        '''
        for pictura in toate_picturile:
            if pictura.get_nume_pictor() not in autori:
                autori[pictura.get_nume_pictor()] = 1
                an_prima_pictura = pictura.get_an()
                cea_mai_recenta = pictura
                autor = pictura.get_nume_pictor()
                for dupaan in toate_picturile:
                    if autor == dupaan.get_nume_pictor() and dupaan.get_an()>an_prima_pictura:
                        cea_mai_recenta = dupaan
                picturi_de_af.append(cea_mai_recenta)
        return picturi_de_af'''
        for pictura in toate_picturile:
            if pictura.get_nume_pictor() not in autori:
                autori[pictura.get_nume_pictor()] = pictura.get_an()
            elif autori[pictura.get_nume_pictor()] < pictura.get_an():
                autori[pictura.get_nume_pictor()] = pictura.get_an()
        rezultat = []
        for autor in autori:
            for dupa_an in toate_picturile:
                if dupa_an.get_nume_pictor() == autor and dupa_an.get_an() == autori[autor]:
                    nume_pictor = autor
                    nume = dupa_an.get_nume()
                    an = autori[autor]
                    pictura_de_adaugat = PictDTO(nume_pictor,nume,an)
                    rezultat.append(pictura_de_adaugat)
        return rezultat
            
        '''
        scopuri = {}
        for eli in elicoptere:
            sir_cu_scopuri = eli.get_scopuri()
            for scop in sir_cu_scopuri:
                if scop not in scopuri:
                    scopuri[scop]=[]
                    for scop_nou in sir_cu_scopuri:
                        if scop_nou.get_scop() == scop:
                            scopuri[scop].append(scop_nou.get_an())'''

