from domain.participant import creeaza_participant, get_id_participant, get_scor, este_interval
from validation.participant_valid import valideaza_participant
from exceptii.erori import ValidError, RepoError
from infrastructura.participanti_repo import adauga_participant_lista
from business.servicii import functionalitate6, functionalitate2a, functionalitate2b, functionalitate2c,functionalitate3a,functionalitate3b,functionalitate3c,functionalitate4a,functionalitate4b,functionalitate4c,functionalitate5a,functionalitate5b

def test_creeaza_participant():
    id_participant = 18
    scor = 32.9
    participant = creeaza_participant(id_participant, scor)
    assert(get_id_participant(participant)==id_participant)
    assert(get_scor(participant)==scor) 

def test_valideaza_participant():
    id_participant = 18
    scor = 32.9
    participant = creeaza_participant(id_participant, scor)
    valideaza_participant(participant)
    id_participant_invalid = "-19"
    scor_invalid = -8
    participant_invalid1 = creeaza_participant(id_participant_invalid, scor)
    try:
        valideaza_participant(participant_invalid1)
        assert(False)
    except ValidError as ve:
        assert(str(ve)=="invalid id participant!\n")
    participant_invalid2 = creeaza_participant(id_participant, scor_invalid)
    try:
        valideaza_participant(participant_invalid2)
        assert(False)
    except ValidError as ve:
        assert(str(ve)=="invalid scor participant!\n")
    participant_invalid3 = creeaza_participant(id_participant_invalid, scor_invalid)
    try:
        valideaza_participant(participant_invalid3)
        assert(False)
    except ValidError as ve:
        assert(str(ve)=="invalid id participant!\ninvalid scor participant!\n")

def test_adauga_participant_lista():
    lista = []
    id_participant = 18
    scor = 32.9
    participant = creeaza_participant(id_participant, scor)
    adauga_participant_lista(lista, participant)
    assert(len(lista)==1)
    assert(get_id_participant(participant)==get_id_participant(lista[0]))
    assert(get_scor(participant)==get_scor(lista[0]))
    participant_acelasi_id = creeaza_participant(id_participant, 72)
    try:
        adauga_participant_lista(lista, participant_acelasi_id)
        assert(False)
    except RepoError as re:
        assert(str(re)=="\nParticipant existent!\n")

def test_funct2a():
    '''l_toti = [[32,54],[434,44],[4,65]]
    care = 434
    functionalitate2a(l_toti,care)
    assert(l_toti==[[32,54],[434,0],[4,65]])
    care = 4
    functionalitate2a(l_toti,care)
    assert(l_toti!=[[32,54],[434,0],[4,65]])'''
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":65}
        ]
    care = 434
    functionalitate2a(l_toti,care)
    assert(l_toti==[
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":0},
        {"id_participant":4, "scor":65}])
    care = 4
    functionalitate2a(l_toti,care)
    assert(l_toti!=[
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":0},
        {"id_participant":4, "scor":65}])
    
    
def test_funct2b():
    '''l_toti = [[32,54],[434,44],[4,65]]
    poz1 = 1
    poz2 = 2
    functionalitate2b(l_toti,poz1,poz2)
    assert(l_toti==[[32,0],[434,0],[4,65]])
    poz1 = 2
    poz2 = 4
    assert(functionalitate2b(l_toti,poz1,poz2)==-1)'''
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":65}
        ]
    poz1 = 1
    poz2 = 2
    id_de_la_care = []
    scoruri_sterse = []
    functionalitate2b(l_toti,poz1,poz2,id_de_la_care,scoruri_sterse)
    assert(l_toti==[
        {"id_participant":32, "scor":0},
        {"id_participant":434, "scor":0},
        {"id_participant":4, "scor":65}
        ])
    poz1 = 2
    poz2 = 4
    assert(functionalitate2b(l_toti,poz1,poz2,id_de_la_care,scoruri_sterse)==-1)
    
    
def test_funct2c():
    '''l_toti = [[32,54],[434,44],[4,65]]
    care = 434
    scor = 32
    functionalitate2c(l_toti,care,scor)
    assert(l_toti==[[32,54],[434,32],[4,65]])
    care = 4
    functionalitate2c(l_toti,care,59)
    assert(l_toti==[[32,54],[434,32],[4,59]])'''
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":65}
        ]
    care = 434
    scor = [3,3,3,3,3,3,3,3,3,5]
    scor_initial = 0
    functionalitate2c(l_toti,care,scor,scor_initial)
    assert(l_toti==[
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":32},
        {"id_participant":4, "scor":65}
        ])
    care = 4
    functionalitate2c(l_toti,care,[6,6,6,6,6,6,6,6,6,5],scor_initial)
    assert(l_toti==[
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":32},
        {"id_participant":4, "scor":59}
        ])

def test_funct3a():
    ''' l_toti = [[32,54],[434,44],[4,65]]
    scor_dat = 55
    assert(functionalitate3a(l_toti,scor_dat)=="32 434 ")
    scor_dat = 14
    assert(functionalitate3a(l_toti,scor_dat)==-1)'''
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":65}]
    scor_dat = 55
    assert(functionalitate3a(l_toti,scor_dat)==[
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44}])
    scor_dat = 14
    assert(functionalitate3a(l_toti,scor_dat)==[])

def test_funct3b():
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":65}
        ]
    li_mea = functionalitate3b(l_toti)
    assert(li_mea==[{"id_participant":434, "scor":44},
        {"id_participant":32, "scor":54},
        {"id_participant":4, "scor":65}])
    
def test_funct3c():
    '''l_toti = [[32,54],[434,44],[4,65]]
    scor_dat = 45
    li_mea = functionalitate3c(l_toti,scor_dat)
    assert(li_mea == [[32,54],[4,65]])
    scor_dat = 70
    li_mea = functionalitate3c(l_toti,scor_dat)
    assert(li_mea == [])'''
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":65}
        ]
    scor_dat = 45
    li_mea = functionalitate3c(l_toti,scor_dat)
    assert(li_mea == [
        {"id_participant":32, "scor":54},
        {"id_participant":4, "scor":65}
        ])
    scor_dat = 70
    li_mea = functionalitate3c(l_toti,scor_dat)
    assert(li_mea == [])
    
    
def test_funct4a():
    '''l_toti = [[32,54],[434,44],[4,66]]
    poz1 = 2
    poz2 = 3
    med = functionalitate4a(l_toti,poz1,poz2)
    assert(med == 55)
    poz1 = 2
    poz2 = 7
    med = functionalitate4a(l_toti,poz1,poz2)
    assert(med == -1)'''
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":66}
        ]
    poz1 = 2
    poz2 = 3
    med = functionalitate4a(l_toti,poz1,poz2)
    assert(med == 55)
    poz1 = 2
    poz2 = 7
    med = functionalitate4a(l_toti,poz1,poz2)
    assert(med == -1)

def test_funct4b():
    l_toti = [
        {"id_participant":32, "scor":50},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":60}
        ]
    poz1 = 1
    poz2 = 3
    scor_minim = functionalitate4b(l_toti,poz1,poz2)
    assert(scor_minim == [{"id_participant":434, "scor":44}])
    l_toti=[
        {"id_participant":32, "scor":41},
        {"id_participant":434, "scor":41},
        {"id_participant":4, "scor":64}
        ]
    poz1 = 1
    poz2 = 3
    scor_minim = functionalitate4b(l_toti,poz1,poz2)
    assert(scor_minim == [{"id_participant":32, "scor":41},{"id_participant":434, "scor":41}])
    poz1 = 2
    poz2 = 2
    scor_minim = functionalitate4b(l_toti,poz1,poz2)
    assert(scor_minim == -1)


def test_funct4c():
    '''l_toti = [[32,50],[434,44],[4,60]]
    poz1 = 1
    poz2 = 3
    multiplu = functionalitate4a(l_toti,poz1,poz2)
    assert(multiplu == "32 4 ")
    l_toti=[[32,52],[434,44],[4,64]]
    poz1 = 1
    poz2 = 3
    multiplu = functionalitate4a(l_toti,poz1,poz2)
    assert(multiplu == -2)
    poz1 = 7
    poz2 = 2
    multiplu = functionalitate4a(l_toti,poz1,poz2)
    assert(multiplu == -1)'''
    l_toti = [
        {"id_participant":32, "scor":50},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":60}
        ]
    poz1 = 1
    poz2 = 3
    multiplu = functionalitate4c(l_toti,poz1,poz2)
    assert(multiplu == [
        {"id_participant":32, "scor":50},
        {"id_participant":4, "scor":60}
        ])
    l_toti=[
        {"id_participant":32, "scor":52},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":64}
        ]
    poz1 = 1
    poz2 = 3
    multiplu = functionalitate4c(l_toti,poz1,poz2)
    assert(multiplu == [])
    poz1 = 7
    poz2 = 2
    multiplu = functionalitate4c(l_toti,poz1,poz2)
    assert(multiplu == -1)

def test_funct5a():
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":66}
        ]
    scor_dat = 6
    li_mea = functionalitate5a(l_toti,scor_dat)
    assert(li_mea == [2])
    scor_dat = 2
    li_mea = functionalitate5a(l_toti,scor_dat)
    assert(li_mea == -1)

def test_funct5b():
    '''
    l_toti = [[32,54],[434,44],[4,65]]
    scor_dat = 45
    li_mea = functionalitate3c(l_toti,scor_dat)
    assert(li_mea == [2])
    scor_dat = 22
    li_mea = functionalitate3c(l_toti,scor_dat)
    assert(li_mea == [])'''
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":66}
        ]
    scor_dat = 45
    li_mea = functionalitate5b(l_toti,scor_dat)
    assert(li_mea == [2])
    scor_dat = 22
    li_mea = functionalitate5b(l_toti,scor_dat)
    assert(li_mea == -1)
    
def test_interval():
    poz1 = 2
    poz2 = 4
    interval = este_interval(poz1,poz2)
    assert(interval)
    
def test_funct6():
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        ]
    stack = [["sterge_participantul_adaugat",32],["sterge_participantul_adaugat",434]]
    functionalitate6(l_toti, stack)
    assert(l_toti==[{"id_participant":32, "scor":54}])
    assert(stack == [["sterge_participantul_adaugat",32]])
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":0},
        ]
    stack = [["sterge_participantul_adaugat",32],["refa_scorul_de_dinainte",434,52]]
    functionalitate6(l_toti, stack)
    assert(l_toti== [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":52},
        ])
    l_toti = [
        {"id_participant":32, "scor":0},
        {"id_participant":434, "scor":0},
        {"id_participant":4, "scor":66}
        ]
    stack = [["sterge_participantul_adaugat",32],["sterge_participantul_adaugat",434],["sterge_participantul_adaugat",4],["sterge_scoruri_din_interval",1,2,[32,434],[23,77]]]
    functionalitate6(l_toti, stack)
    assert(l_toti==[
        {"id_participant":32, "scor":23},
        {"id_participant":434, "scor":77},
        {"id_participant":4, "scor":66}
        ])
    assert(stack==[["sterge_participantul_adaugat",32],["sterge_participantul_adaugat",434],["sterge_participantul_adaugat",4]])
    l_toti = [
        {"id_participant":32, "scor":54},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":78}
        ]
    stack= [["sterge_participantul_adaugat",32],["sterge_participantul_adaugat",434],["sterge_participantul_adaugat",4],["modifica_scorul",32,91]]
    functionalitate6(l_toti, stack)
    assert(l_toti==[
        {"id_participant":32, "scor":91},
        {"id_participant":434, "scor":44},
        {"id_participant":4, "scor":78}
        ])

def run_teste():
    #print("Aici incepem testele.")
    test_creeaza_participant()
    test_valideaza_participant()
    test_adauga_participant_lista()
    test_interval()
    test_funct2a()
    test_funct2b()
    test_funct2c()
    test_funct3a()
    test_funct3b()
    test_funct3c()
    test_funct4a()
    test_funct4b()
    test_funct4c()
    test_funct5a()
    test_funct5b()
    test_funct6()
    #print("Aici terminam testele.")


