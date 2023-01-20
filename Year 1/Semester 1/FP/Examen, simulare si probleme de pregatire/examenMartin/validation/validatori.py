from erors.exceptii import ValidError
class ValidatorEveniment(object):
    

    def valideaza(self,eveniment):
        #valideaza datele unui eveniment
        #input:eveniment
        #ecxpetii : ceva nu e conform => ValidError
        err = ""
        zile_in_luna = [0,31,28,31,30,31,30,31,31,30,31,30,31]
        data = eveniment.get_data()
        parti = data.split(".")
        if len(parti)==3:
            try:
                zi = int(parti[0])
                luna = int(parti[1])
                an = int(parti[2])
                if luna <1 or luna >12:
                    err += "Data invalida!\n"
                else:
                    if zi <0 or zi>zile_in_luna[luna]:
                        err += "Data invalida!\n"
                    else:
                        if an <1 :
                            err += "Data invalida!\n"
            except: 
                err += "Format data invalid!\n"
        ora = eveniment.get_ora()
        parti2 = ora.split(".")
        if len(parti2)==2:
            try:
                hh = int(parti2[0])
                mm = int(parti2[1])
                if hh<0 or hh>23:
                    err += "Ora invalida!\n"
                if mm<0 or mm>59:
                    err+= "Minute invalide!\n"
            except:
                err+= "Format ora invalid!\n"
        if eveniment.get_descriere()=="":
            err+= "Descriere invalida!\n"
        if len(err)>0:
            raise ValidError(err)

    def valideaza_data(self,data):
        #functie care valideaza o data
        #input: data
        #ecxpetii : ceva nu e conform => ValidError
        err = ""
        zile_in_luna = [0,31,28,31,30,31,30,31,31,30,31,30,31]
        parti = data.split(".")
        if len(parti)==3:
            try:
                zi = int(parti[0])
                luna = int(parti[1])
                an = int(parti[2])
                if luna <1 or luna >12:
                    err += "Data invalida!\n"
                else:
                    if zi <0 or zi>zile_in_luna[luna]:
                        err += "Data invalida!\n"
                    else:
                        if an <1 :
                            err += "Data invalida!\n"
            except: 
                err += "Format data invalid!\n"
        if len(err)>0:
            raise ValidError(err)
