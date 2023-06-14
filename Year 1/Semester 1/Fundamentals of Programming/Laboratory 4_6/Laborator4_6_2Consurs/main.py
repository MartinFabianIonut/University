'''
Created on 21 oct. 2021

@author: marti
'''
from testare.teste import run_teste
from prezentare.interfata import run
from prezentarebatchmode.interfatabatchmode import runbatchmode

if __name__ == '__main__':
    run_teste()
    print("Ce fel de meniul vrei? 1 - normal sau 2 - batch mode")
    cmd = input(">>>")
    if cmd == "1":
        run()
    elif cmd == "2":
        runbatchmode()
    else:
        print("Nu exista asa ceva!")