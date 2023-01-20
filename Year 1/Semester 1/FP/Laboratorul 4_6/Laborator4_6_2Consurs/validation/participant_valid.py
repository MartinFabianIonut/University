from domain.participant import get_id_participant,get_scor
from exceptii.erori import ValidError

def valideaza_participant(participant):
    err = ""
    if type(get_id_participant(participant))==str:
        err += "invalid id participant!\n"
    else:
        if get_id_participant(participant)<=0:
            err += "invalid id participant!\n"
    if get_scor(participant)<0:
        err += "invalid scor participant!\n"
    if len(err)>0:
        raise ValidError(err)

def valideaza_scor(scor):
    err = ""
    for i in range(len(scor)):
        if type(scor[i])==str:
            err+="Scorul "+str(i)+" este invalid, nefiind o valoare numerica!\n"
        if scor[i]<1 or scor[i]>10:
            err+="Scorul "+str(i)+" este invalid, fiind o valoare numerica care nu apartine intervalului [1,10] !\n"
    if len(err)>0:
        raise ValidError(err)
    
def exista_participant(lista,care):
    ce_scor_are_care = 0
    bulean = False
    for _participant in lista:
        if care==_participant["id_participant"]:
            bulean = True
            ce_scor_are_care = _participant["scor"]
    if bulean == False:
        raise ValidError("\nNu exista asemenea participant!\n")
    return ce_scor_are_care