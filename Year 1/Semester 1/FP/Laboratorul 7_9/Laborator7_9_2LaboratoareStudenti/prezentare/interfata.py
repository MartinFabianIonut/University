from erors.exceptii import ValidationError, RepositoryError
class Consola(object):
    
    
    def __init__(self, srv_student, srv_lab,srv_note):
        self.__srv_student = srv_student
        self.__srv_lab = srv_lab
        self.__srv_note = srv_note

    
    def __ui_adauga_student(self):
        try:
            id_student = int(input("Introdu id student: "))
        except ValueError:
            print("Id numeric invalid!\n")
            return
        nume = input("Introdu numele studentului: ")
        try:
            grupa = int(input("Grupa studentului: "))
        except ValueError:
            print("Valoare numeric invalida pentru grupa!\n")
            return
        self.__srv_student.adauga_student(id_student,nume,grupa)
        print("Student adaugat cu succes!\n")
    
    
    def __ui_sterge_student(self):
        try:
            id_student = int(input("Introdu id-ul studentului pe care doresti sa il stergi: "))
        except ValueError:
            print("Id numeric invalid!\n")
            return
        self.__srv_student.sterge_student(id_student)
        print("Studentul cu id-ul: " + str(id_student) + " a fost sters cu succes!\n")
        try:
            self.__srv_note.sterge_student_nota(id_student)
        except:
            print("Studentul cu id-ul dat nu a avut asignata nicio nota, deci nu a fost nevoie sa se stearga vreo nota!")
    

    def __ui_modifica_nume_student(self):
        try:
            id_student = int(input("Introdu id-ul studentului al carui nume doresti sa il modifici: "))
        except ValueError:
            print("Id numeric invalid!\n")
            return 
        nume_nou = input("Introdu noul nume: ")
        self.__srv_student.modifica_nume_student(id_student,nume_nou)
        print("Numele studentului cu id-ul: " + str(id_student) + " a fost modificat cu succes!\n")
    
    
    def __ui_modifica_grupa_student(self):
        try:
            id_student = int(input("Introdu id-ul studentului a carui grupa doresti sa o modifici: "))
        except ValueError:
            print("Id numeric invalid!\n")
            return
        try:
            grupa_noua = int(input("Introdu noua grupa: "))
        except ValueError:
            print("Id numeric invalid!\n")
            return 
        self.__srv_student.modifica_grupa_student(id_student,grupa_noua)
        print("Grupa studentului cu id-ul: " + str(id_student) + " a fost modificata cu succes!\n")
    
    
    def __ui_print_student(self):
        lista_toti_studentii = self.__srv_student.get_all_studenti()
        if len(lista_toti_studentii)>0:
            print("\nLista cu toti studentii: \n")
            for _student in lista_toti_studentii:
                print(str(_student))
            print("")
        else:
            print("Nu exista studenti!\n")
            
    def __ui_print_student_aflabetic(self):
        lista_toti_studentii = self.__srv_student.sorteaza_alfabetic()
        if len(lista_toti_studentii)>0:
            print("\nLista cu toti studentii: \n")
            for _student in lista_toti_studentii:
                print(str(_student))
        else:
            print("Nu exista studenti!\n")
    
    def __ui_cauta_student(self):
        try:
            id_student = int(input("Introdu id-ul studentului cautat: "))
        except ValueError:
            print("Id numeric invalid!\n")
            return
        gasit = self.__srv_student.cauta_student(id_student)
        print("\nStudentul cautat:\n" + str(gasit) + "\n")
    
    def __ui_adauga_lab(self):
        nrlab_nrpb = input("Introdu id lab, sub forma numar laborator + <<bara jos>> + numar problema. Unde e numar se asteapta un int mai mare decat 0, iar intre cele 3 elemente nu se pune spatiu (ex: 12_4)): ")
        descriere = input("Introdu descrierea: ")
        try:
            deadline = []
            deadline.append(int(input("Introdu ziua deadline-ului: ")))
            deadline.append(int(input("Introdu luna deadline-ului: ")))
            deadline.append(int(input("Introdu anul deadline-ului: ")))
        except ValueError:
            print("Valoare numeric invalida pentru una dintre categoriile deadlineului (zi, luna sau an)!\n")
            return
        self.__srv_lab.adauga_lab(nrlab_nrpb,descriere,deadline)
        print("Laborator adaugat cu succes!\n")
    
    
    def __ui_sterge_lab(self):
        nrlab_nrpb = input("Introdu id-ul laboratorului pe care vrei sa il stergi: ")
        self.__srv_lab.sterge_lab(nrlab_nrpb)
        print("Laboratorul cu id-ul: " + nrlab_nrpb + " a fost sters cu succes!\n")
        try:
            self.__srv_note.sterge_lab_nota(nrlab_nrpb)
        except:
            print("Laboratorul cu id-ul dat nu a avut asignata nicio nota, deci nu a fost nevoie sa se stearga vreo nota!")
    
    
    def __ui_modifica_descriere_lab(self):
        nrlab_nrpb = input("Introdu id-ul laboratorului a carui descriere doresti sa o modifici: ")
        descriere_noua = input("Introdu noua descriere: ")
        self.__srv_lab.modifica_descriere_lab(nrlab_nrpb,descriere_noua)
        print("Descrierea laboratorului cu id-ul: " + nrlab_nrpb + " a fost modificata cu succes!\n")
    
    
    def __ui_modifica_deadline_lab(self):
        nrlab_nrpb = input("Introdu id-ul laboratorului al carui deadline doresti sa il modifici: ")
        try:
            deadline_nou = []
            deadline_nou.append(int(input("Introdu ziua noului deadline: ")))
            deadline_nou.append(int(input("Introdu luna noului deadline: ")))
            deadline_nou.append(int(input("Introdu anul noului deadline: ")))
        except ValueError:
            print("Valoare numeric invalida pentru una dintre categoriile deadlineului (zi, luna sau an)!\n")
            return 
        self.__srv_lab.modifica_deadline_lab(nrlab_nrpb,deadline_nou)
        print("Deadline-ul laboratoului cu id-ul: " + nrlab_nrpb + " a fost modificat cu succes!\n")
    
    
    def __ui_print_lab(self):
        lista_toate_lab = self.__srv_lab.get_all_lab()
        if len(lista_toate_lab)>0:
            print("\nLista cu toate laboratoarele: \n")
            for _lab in lista_toate_lab:
                print(str(_lab))
            print("")
        else:
            print("Nu exista laboratoare!\n")
    
    def __ui_cauta_lab(self):
        nrlab_nrpb = input("Introdu id-ul laboratorului cautat: ")
        gasit = self.__srv_lab.cauta_lab(nrlab_nrpb)
        print("\nLaboratorul cautat:\n" + str(gasit) + "\n")
    
    
    def __ui_filtreaza_dupa_nume(self):
        prefix = input("Introdu prefixul dupa care doresti sa se faca filtrarea numelor studentilor: ")
        studenti = self.__srv_student.filtreaza_nume(prefix)
        if studenti == []:
            print("Nu exsita caazuri de filtrare!\n")
        else:
            print("Cazurile de filtrare:")
            for _student in studenti:
                print(str(_student))
                

    def __ui_asignare_nota(self):
        try:
            id_nota = int(input("Introdu id nota: "))
            id_student = int(input("Introdu id-ul studentului caruia vrei sa ii dai nota (nr intreg pozitiv): "))
        except ValueError:
            print("Id numeric invalid!\n")
            return
        nrlab_nrpb = input("Introdu id lab, sub forma numar laborator + <<bara jos>> + numar problema. Unde e numar se asteapta un int mai mare decat 0, iar intre cele 3 elemente nu se pune spatiu (ex: 12_4)): ")
        self.__srv_note.asignare_nota(id_nota,id_student,nrlab_nrpb)
        print("Laborator asignat cu succes!\n")
        
    def __ui_adauga_nota(self):
        try:
            id_nota = int(input("Introdu id nota: "))
        except ValueError:
            print("Id numeric invalid!\n")
            return
        try:
            nota_pb = float(input("Introdu nota (nr real mai mare sau egal cu 1 si mai mic sau egal cu 10): "))
        except ValueError:
            print("Nota invalida!\n")
            return
        self.__srv_note.adauga_nota(id_nota,nota_pb)
        print("Nota adaugata cu succes!\n")
    
    def __ui_sterge_nota(self):
        try:
            id_nota = int(input("Introdu id nota: "))
        except ValueError:
            print("Id numeric invalid!\n")
            return
        self.__srv_note.sterge_nota(id_nota)
        print("Nota cu id-ul: " + str(id_nota) + " a fost stearsa cu succes!\n")
    
    def __ui_print_note(self):
        lista_toate_notele = self.__srv_note.get_all_for_files()
        if len(lista_toate_notele)>0:
            for nota in lista_toate_notele:
                print(nota)
        else:
            print("Nu exista note!\n")
    
    
    def __ui_random_student(self,parti):
        try:
            cate = int(parti[2])
        except ValueError:
            print("Id numeric invalid!\n")
            return
        for i in range(cate):
            id_student = self.__srv_student.get_id_random()
            nume = self.__srv_student.get_string_random()
            grupa = self.__srv_student.get_id_random()
            self.__srv_student.adauga_student(id_student,nume,grupa)
    
    
    def __ui_random_lab(self, parti):
        try:
            cate = int(parti[2])
        except ValueError:
            print("Id numeric invalid!\n")
            return
        for i in range(cate):
            nrlab_nrpb1 = self.__srv_lab.get_id_random()
            nrlab_nrpb2 = self.__srv_lab.get_id_random()
            nrlab_nrpb = ""
            nrlab_nrpb = nrlab_nrpb + str(nrlab_nrpb1) + "_" + str(nrlab_nrpb2)
            descriere = self.__srv_lab.get_string_random()
            deadline = self.__srv_lab.get_data_random()
            self.__srv_lab.adauga_lab(nrlab_nrpb,descriere,deadline)
    
    
    def __ui_random_nota(self, parti):
        try:
            cate = int(parti[2])
        except ValueError:
            print("Id numeric invalid!\n")
            return
        for i in range(cate):
            try:
                id_nota = self.__srv_note.get_id_random()
                id_student = self.__srv_note.get_id_random_st()
                nrlab_nrpb = self.__srv_note.get_string_random()
                self.__srv_note.asignare_nota(id_nota,id_student,nrlab_nrpb)
            except RepositoryError as re:
                print("\nSe intampina o eroare la nivelul repozitoriului de studenti: \n" + str(re))
            finally:
                continue
    
    
    def __ui_print_raport1a(self):
        nrlab_nrpb = input("Dati id-ul problemei pentru care doriti sa vedeti lista de studenti si notele lor, ordonati alfabetic dupa nume: ")
        alfabetic = self.__srv_note.raport_studenti_cu_note_alfabetic(nrlab_nrpb)
        print("\nMai jos este lista de studenti si notele lor la o problema de laborator dat, ordonati alfabetic dupa nume:\n")
        if len(alfabetic)>0:
            for nota in alfabetic:
                print("\t"+str(nota))
            print("")
    
    
    def __ui_print_raport1b(self):
        nrlab_nrpb = input("Dati id-ul problemei pentru care doriti sa vedeti lista de studenti si notele lor, ordonati descrescator dupa nota: ")
        descrescator_dupa_nota = self.__srv_note.raport_studenti_cu_note_dupa_nota(nrlab_nrpb)
        print("\nMai jos este lista de studenti si notele lor la o problema de laborator dat, ordonati descrescator dupa nota:\n")
        if len(descrescator_dupa_nota)>0:
            for nota in descrescator_dupa_nota:
                print("\t"+str(nota))
            print("")
    
    
    def __ui_print_raport2(self):
        medii = self.__srv_note.raport_studenti_medie_sub_cinci()
        if len(medii)>0:
            print("")
            for media in medii:
                print("Studentul:  " + str(media[0]) +" are media notelor de laborator: " + str(media[1]))
            print("")
        else:
            print("\nNu sunt studenti cu media notelor mai mica decat 5!\n")
    
    
    def __ui_print_raport_nou(self):
        primele_cincizeci_la_suta = self.__srv_note.raport_primele_cincizeci_la_suta_note_lab_medie_peste_cinci()
        if len(primele_cincizeci_la_suta)>0:
            print("")
            for lab in primele_cincizeci_la_suta:
                print(lab)
            print("")
        else:
            print("\nNu exista laboratoare notate.\n")
    
    
    
    def meniul_meu(self):
        # afiseaza optiunile aplicatiei - adica functionalitatile
        print("Aplicatia pentru laboratoarele 7-9")
        print("1. Functionalitati legate de student:\n\t-> adauga_student, sterge_student, modifica_nume, modifica_grupa, cauta_student, print_studenti")
        print("2. Functionalitati legate de probleme de laborator:\n\t-> adauga_lab, sterge_lab, modifica_descriere, modifica_deadline, cauta_lab, print_lab")
        print("3. Functionalitati legate de notare/asignare:\n\t-> asignare_lab, adauga_nota, print_note")
        print("4. Generari random: random_student_x, random_lab_x, random_nota_x (unde x este numarul de generari)")
        print("5. Rapoarte:\n\t-> raport1a = lista de studenti si notele lor la o problema de laborator dat, ordonati alfabetic dupa nume\n\t-> raport1b = lista de studenti si notele lor la o problema de laborator dat, ordonati descrescator dupa nota\n\t-> raport2 = toti studentii cu media notelor de laborator mai mica decat 5\n\t-> raport_nou")
    
    def run(self):
        while True:
            self.meniul_meu()
            cmd = input("Introdu o comanda: ")
            if cmd == "exit":
                return
            elif cmd == "":
                continue
            elif cmd == "adauga_student":
                try:
                    self.__ui_adauga_student()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de studenti: \n" + str(re))
            elif cmd == "sterge_student":
                try:
                    self.__ui_sterge_student()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de studenti: \n" + str(re))
            elif cmd =="modifica_nume":
                try:
                    self.__ui_modifica_nume_student()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de studenti: \n" + str(re))
            elif cmd =="modifica_grupa":
                try:
                    self.__ui_modifica_grupa_student()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de studenti: \n" + str(re))
            elif cmd == "cauta_student":
                try:
                    self.__ui_cauta_student()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de studenti: \n" + str(re))
            elif cmd == "print_studenti":
                self.__ui_print_student()
            elif cmd == "print_al":
                self.__ui_print_student_aflabetic()
            elif cmd == "adauga_lab":
                try:
                    self.__ui_adauga_lab()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de laboratoare: \n" + str(re))
            elif cmd == "sterge_lab":
                try:
                    self.__ui_sterge_lab()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de laboratoare: \n" + str(re))
            elif cmd =="modifica_descriere":
                try:
                    self.__ui_modifica_descriere_lab()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de laboratoare: \n" + str(re))
            elif cmd =="modifica_deadline":
                try:
                    self.__ui_modifica_deadline_lab()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de laboratoare: \n" + str(re))
            elif cmd == "print_lab":
                self.__ui_print_lab()
            elif cmd == "cauta_lab":
                try:
                    self.__ui_cauta_lab()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de laboratoare: \n" + str(re))
            elif cmd == "filter_nume":
                try:
                    self.__ui_filtreaza_dupa_nume()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de studenti: \n" + str(re))
            elif cmd == "asignare_lab":
                try:
                    self.__ui_asignare_nota()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de note: \n" + str(re))
            elif cmd == "adauga_nota":
                try:
                    self.__ui_adauga_nota()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de note: \n" + str(re))
            elif cmd == "sterge_nota":
                try:
                    self.__ui_sterge_nota()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de note: \n" + str(re))
            elif cmd == "print_note":
                self.__ui_print_note()
            elif cmd =="pp":
                lista_toate_notele = self.__srv_note.get_all_note_sir()
                if len(lista_toate_notele)>0:
                    for nota in lista_toate_notele:
                        print(str(nota))
                else:
                    print("Nu exista note!\n")
            elif cmd == "raport1a":
                try:
                    self.__ui_print_raport1a()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de note: \n" + str(re))
            elif cmd == "raport1b":
                try:
                    self.__ui_print_raport1b()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de note: \n" + str(re))
            elif cmd == "raport2":
                try:
                    self.__ui_print_raport2()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de note: \n" + str(re))
            elif cmd == "raport_nou":
                try:
                    self.__ui_print_raport_nou()
                except ValidationError as ve:
                    print("\nSe intampina o eroare la validare: \n" + str(ve))
                except RepositoryError as re:
                    print("\nSe intampina o eroare la nivelul repozitoriului de note: \n" + str(re))
            elif cmd != "":
                parti = []
                parti = cmd.split("_")
                if len(parti)==3:
                    if parti[0]=="random":
                        if parti[1]=="student":
                            try:
                                self.__ui_random_student(parti)
                            except ValidationError as ve:
                                print("\nSe intampina o eroare la validare: \n" + str(ve))
                            except RepositoryError as re:
                                print("\nSe intampina o eroare la nivelul repozitoriului de studenti: \n" + str(re))                        
                        elif parti[1]=="lab":
                            try:
                                self.__ui_random_lab(parti)
                            except ValidationError as ve:
                                print("\nSe intampina o eroare la validare: \n" + str(ve))
                            except RepositoryError as re:
                                print("\nSe intampina o eroare la nivelul repozitoriului de laboratoare: \n" + str(re))  
                        elif parti[1]=="nota":
                            try:
                                self.__ui_random_nota(parti)
                            except ValidationError as ve:
                                print("\nSe intampina o eroare la validare: \n" + str(ve))
                            except RepositoryError as re:
                                print("\nSe intampina o eroare la nivelul repozitoriului de note: \n" + str(re)) 
                else:
                    print("Comanda invalida!")
            else:
                print("Comanda invalida!")
    
    
    
    



