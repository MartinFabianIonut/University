'''
Created on 6 dec. 2021

@author: marti
'''
from validation.validatori import ValidatorPictura
from repository.repozitorii import FileRepoPicturi
from controller.servicii import ServicePicturi
from prezentare.interfata import Consola
from testare.teste import Teste


if __name__ == '__main__':
    valid_pictura = ValidatorPictura()
    repo_picturi = FileRepoPicturi("picturi.txt")
    
    srv_picturi = ServicePicturi(valid_pictura,repo_picturi)
    
    ui = Consola(srv_picturi)
    
    teste = Teste()
    teste.run_all_tests()
    
    ui.run()