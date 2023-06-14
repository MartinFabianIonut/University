'''
Created on 10 nov. 2021

@author: marti
'''

from prezentare.interfata import Consola
from controller.servicii import ServiceStudent, ServiceLaborator, ServiceNote
from repository.repozitorii import FileRepoStudent, FileRepoLaborator, FileRepoNote
from validation.validatori import ValidatorStudent, ValidatorLaborator, ValidatorNote
from tests.teste import Teste

if __name__ == '__main__':
    valid_student = ValidatorStudent()
    valid_lab = ValidatorLaborator()
    valid_note = ValidatorNote()
    
    repo_student = FileRepoStudent("studenti.txt")
    repo_lab = FileRepoLaborator("laboratoare.txt")
    repo_note = FileRepoNote("note.txt")
    
    srv_student = ServiceStudent(valid_student, repo_student)
    srv_lab = ServiceLaborator(valid_lab, repo_lab)
    srv_note = ServiceNote(valid_note, repo_note, repo_student, repo_lab)
    
    teste = Teste()
    teste.run_all_tests()
    
    
    ui = Consola(srv_student, srv_lab, srv_note)
    ui.run()