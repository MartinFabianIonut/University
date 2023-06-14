from exceptii.erori import RepoError
from domain.participant import participanti_egali

def adauga_participant_lista(lista, participant):
    for _participant in lista:
        if participanti_egali(_participant,participant):
            raise RepoError("\nParticipant existent!\n")
    lista.append(participant)
    
