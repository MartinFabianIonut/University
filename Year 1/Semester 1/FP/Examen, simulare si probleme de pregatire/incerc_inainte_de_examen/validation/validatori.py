from erors.exceptii import ValidError
class ValidatorPiesa(object):
    
    def valideaza(self, piesa):
        err = ""
        if piesa.get_titlu()=="":
            err += "Titlu invalid!\n"
        if piesa.get_regizor()=="":
            err += "Nume regizor invalid!\n"
        if piesa.get_gen()=="":
            err += "Gen invalid!\n"
        if piesa.get_durata()<1:
            err += "Durata invalida!\n"
        if len(err)>0:
            raise ValidError(err)



