'''
Created on 8 dec. 2021

@author: marti
'''

from testare.teste import Teste
from validation.validatori import ValidatorConcurenti, ValidatorParticipari
from repository.repozitorii import FileRepoConcurenti, FileRepoParticipari
from controller.servicii import ServiceConcurenti, ServiceParticipari
from domanin.prezentare import Consola

if __name__ == '__main__':
    valid_concurent = ValidatorConcurenti()
    valid_participare = ValidatorParticipari()
    
    repo_concurenti = FileRepoConcurenti("concurenti.txt")
    repo_participari = FileRepoParticipari("participari.txt")
    
    srv_concurent = ServiceConcurenti(valid_concurent,repo_concurenti)
    srv_participare = ServiceParticipari(valid_participare,repo_participari, repo_concurenti)
    
    ui = Consola(srv_concurent,srv_participare)
    
    teste = Teste()
    teste.run_all_tests()
    
    ui.run()