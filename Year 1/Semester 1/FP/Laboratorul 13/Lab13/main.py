'''
Created on 5 ian. 2022

@author: marti
'''

from prezentare.interfata import Consola
from teste.tests import Teste

if __name__ == '__main__':
    
    test = Teste()
    test.run_all_tests()
    
    ui = Consola()
    ui.run()