'''
Created on 16 ian. 2022

@author: marti
'''

from prezentare.inferfata import Console
from validation.validatori import ValidatorPiesa
from repository.repozitorii import FileRepoPiese
from controller.servicii import ServicePiesa
from testare.teste import Teste


if __name__ == '__main__':
    val = ValidatorPiesa()
    
    repo = FileRepoPiese("piese.txt")
    
    srv = ServicePiesa(val,repo)
    
    teste = Teste()
    teste.run_all_tests()
    
    ui = Console(srv)
    ui.run()