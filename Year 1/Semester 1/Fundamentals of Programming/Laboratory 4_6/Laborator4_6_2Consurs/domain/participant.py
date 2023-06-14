def creeaza_participant(id_participant, scor):
    #return [id_participant,scor]
    return {
        "id_participant": id_participant,
        "scor": scor
        }

def get_id_participant(participant):
    return participant["id_participant"]

def get_scor(participant):
    return participant["scor"]

def participanti_egali(p1, p2):
    return get_id_participant(p1)==get_id_participant(p2)

def este_interval(poz1, poz2):
    return poz1<poz2


