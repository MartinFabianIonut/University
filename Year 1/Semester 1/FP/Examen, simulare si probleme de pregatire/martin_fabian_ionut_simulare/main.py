'''
Created on 10 dec. 2021

@author: marti
'''

from prezentare.interfata import Consola
from controller.servicii import ServiceCarti, ServiceImprumut
from validation.validatori import ValidatorCarte, ValidatorImprumut
from repository.repozitorii import FileRepoCarte, FileRepoImprumut
from testare.teste import Teste

if __name__ == '__main__':
    valid_carte = ValidatorCarte()
    valid_imprumut = ValidatorImprumut()
    
    repo_carte = FileRepoCarte("carti.txt")
    repo_imprumut = FileRepoImprumut("imprumuturi.txt")
    
    srv_carte = ServiceCarti(valid_carte,repo_carte)
    srv_imprumut = ServiceImprumut(valid_imprumut,repo_imprumut,repo_carte)
    
    teste = Teste()
    teste.run_all_tests()
    
    ui = Consola(srv_carte,srv_imprumut)
    ui.run()
    