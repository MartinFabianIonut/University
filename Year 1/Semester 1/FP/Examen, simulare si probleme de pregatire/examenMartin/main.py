'''
Created on 17 ian. 2022

@author: marti
'''

from prezentare.interfata import Console
from validation.validatori import ValidatorEveniment
from repository.repozitorii import FileRepoEvenimente
from controller.servicii import ServiceEvenimente
from testare.teste import Teste

if __name__ == '__main__':
    valid = ValidatorEveniment()
    repo = FileRepoEvenimente("evenimente.txt")
    srv = ServiceEvenimente(valid,repo)
    
    teste = Teste()
    teste.run_all_tests()
    
    ui = Console(srv)
    ui.run()