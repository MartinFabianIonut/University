from erors.exceptii import ValidationError

class ValidatorLaborator(object):
    def valideaza(self, lab):
        err = ""
        if lab.get_nrlab_nrpb()=="":
            err += "Id invalid!\n"
        else:
            id_lab = lab.get_nrlab_nrpb()
            parti = []
            parti = id_lab.split("_")
            try:
                assert(int(parti[0])>0)
                assert(int(parti[1])>0)
            except:
                err += "Id invalid!\n"
        if lab.get_descriere()=="":
            err += "Descriere invalida!\n"
        deadline = lab.get_deadline()
        if deadline == []:
            err += "Deadline invalid!\n"
        elif deadline[0]<1 or deadline[0]>31 or deadline[1]<1 or deadline[1]>12 or deadline[2]<2021:
            err += "Deadline invalid!\n"
        if len(err)>1:
            raise ValidationError(err)
    
    def valideaza_id(self, nrlab_nrpb):
        err = ""
        if nrlab_nrpb=="":
            err += "Id invalid!\n"
        else:
            id_lab = nrlab_nrpb
            parti = []
            parti = id_lab.split("_")
            try:
                assert(int(parti[0])>0)
                assert(int(parti[1])>0)
            except:
                err += "Id invalid!\n"
        if len(err)>1:
            raise ValidationError(err)
        
    def valideaza_descriere_noua(self, descriere_noua):
        err = ""
        if descriere_noua=="":
            err += "Descriere invalida!\n"
        if len(err)>1:
            raise ValidationError(err)
        
    def valideaza_deadline_nou(self, deadline_nou):
        err = ""
        if len(deadline_nou)!=3:
            err += "Deadline invalid!\n"
        elif deadline_nou[0]<1 or deadline_nou[0]>31 or deadline_nou[1]<1 or deadline_nou[1]>12 or deadline_nou[2]<2021:
            err += "Deadline invalid!\n"
        if len(err)>1:
            raise ValidationError(err)


class ValidatorStudent(object):
    
    
    def valideaza(self, student):
        err = ""
        if student.get_id_student()<0:
            err += "Id invalid!\n"
        if student.get_nume()=="":
            err += "Nume invalid!\n"
        if student.get_grupa()<0:
            err += "Grupa invalida!\n"
        if len(err)>1:
            raise ValidationError(err)
    
    def valideaza_id_(self, id_student):
        err = ""
        if id_student<0:
            err += "Id invalid!\n"
        if len(err)>1:
            raise ValidationError(err)
        
    def valideaza_nume_nou(self, nume_nou):
        err = ""
        if nume_nou=="":
            err += "Nume invalid!\n"
        if len(err)>1:
            raise ValidationError(err)
        
    def valideaza_grupa_noua(self, grupa_noua):
        err = ""
        if grupa_noua<0:
            err += "Grupa invalida!\n"
        if len(err)>1:
            raise ValidationError(err)
    
    def valideaza_prefix(self, prefix):
        err = ""
        if prefix=="":
            err += "Prefix invalid!\n"
        if len(err)>1:
            raise ValidationError(err)



class ValidatorNote(object):
    
    def valideaza_id(self, nota):
        err = ""
        if nota.get_id_nota()<0:
            err += "Id invalid!\n"
        if len(err)>1:
            raise ValidationError(err)
    
    def valideaza(self, nota,nota_pb):
        err = ""
        if nota.get_id_nota()<0:
            err += "Id invalid!\n"
        if nota_pb<1 or nota_pb>10:
            err += "Nota invalida!\n"
        if len(err)>1:
            raise ValidationError(err)
    
    def valideaza_id_nota(self, id_nota):
        err = ""
        if id_nota<0:
            err += "Id nota laborator invalid!\n"
        if len(err)>1:
            raise ValidationError(err)
        
    def valideaza_id_student(self, id_student):
        err = ""
        if id_student<0:
            err += "Id student invalid!\n"
        if len(err)>1:
            raise ValidationError(err)
        
    def valideaza_id_lab(self, nrlab_nrpb):
        err = ""
        if nrlab_nrpb=="":
            err += "Id lab invalid!\n"
        else:
            id_lab = nrlab_nrpb
            parti = []
            parti = id_lab.split("_")
            try:
                assert(int(parti[0])>0)
                assert(int(parti[1])>0)
            except:
                err += "Id lab invalid!\n"
        if len(err)>1:
            raise ValidationError(err)

