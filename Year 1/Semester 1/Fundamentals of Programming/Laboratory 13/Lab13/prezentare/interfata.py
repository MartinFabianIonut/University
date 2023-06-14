
class Consola(object):
   
    
    def __suma(self, lista):
        suma = 0
        for i in lista:
            suma += i
        return suma
    
    
    def __e_prim(self, nr):
        if nr < 2:
            return False
        if nr > 2:
            for i in range(2, nr):
                if (nr % i) == 0:
                    return False
        return True
    
    
    def __valid(self, k,cmd, lista):
        if self.__suma(lista)>cmd:
            return False
        if k>0:
            if lista[k]<lista[k-1]:
                return False
        return True
            
    
    
    def __print_solutie(self, k, cmd, lista):
        if len(lista)>1:
            sir = ""
            for i in range(k):
                sir += str(lista[i])
                sir += "+"
            sir +=  str(lista[k])
            sir += "="
            sir += str(cmd)
            print(sir)
    
    
    def __back_recursiv(self, k, cmd, lista):
        lista.append(0)
        for i in range (cmd - self.__suma(lista)+1):
            if self.__e_prim(i)==True:
                lista[-1]=i
                if self.__valid(k,cmd,lista)==True:
                    if self.__suma(lista)==cmd:
                        self.__print_solutie(k,cmd,lista)
                    else:
                        self.__back_recursiv(k+1, cmd, lista)
        lista.pop()
        
        
        
        
    def __valid_iter(self, k,cmd, lista):
        for el in lista:
            if self.__e_prim(el)==False:
                return False
        if self.__suma(lista)>cmd:
            return False
        if k>0:
            if lista[k]<lista[k-1]:
                return False
        return True
    
    def __mai_sunt_valori(self, k, x,cmd):
        return x[k]<=cmd
        
    def __back_iterativ(self,k,cmd,x):
        x.append(0) 
        while k>-1:
            if self.__mai_sunt_valori(k, x, cmd)==True:
                x[k] += 1
                if self.__valid_iter(k,cmd,x) == True:
                    if self.__suma(x)==cmd:
                        self.__print_solutie(k,cmd,x)
                    else:
                        k += 1
                        x.append(0)
            else:
                k -= 1
                x.pop()
                
    
    '''
    Rezolvare backtracking descompunere numar intreg in sume de numere prime
    
    solutie candidat: 
    x = (x0, x1,. .. , xk), xi apartine numere prime <= n, Orice i apartine 0,...k
    
    conditie consistent: 
    x = (x0, x1,. .. , xk) e consistent daca suma elementelor lui x <= numarul citit + x0 <= x1 <= ... <= xk
    
    conditie solutie:      
    x= (x0, x1,. .. , xk) e solutie daca e consistent si suma elementelor lui x == numarul citit, k>=2
    
    '''
    
    
    def __ui_descompunere(self, cmd_init):
        try:
            cmd = int(cmd_init)
        except ValueError:
            print("Numar invelid!")
            return
        lista = []
        #self.__back_recursiv(0,cmd,lista)
        x = []
        self.__back_iterativ(0, cmd,x)
        
        
                
        
    def __inter(self, l, start, end, mij):
        i = start
        j = mij + 1
        k = start
        aux = [None]*len(l)
        while i<= mij and j <= end:
            if l[i]<l[j]:
                aux[k] = l[i]
                k+=1
                i+=1
            else:
                aux[k]=l[j]
                k+=1
                j+=1
        while i<=mij:
            aux[k] = l[i]
            k+=1
            i+=1
        while j<=end:
            aux[k]=l[j]
            k+=1
            j+=1
        for i in range(start,end+1):
            l[i]=aux[i]
    
    
    def __merge(self, l, start, end):
        if start < end:
            mij = (start + end )//2
            self.__merge(l,start,mij)
            self.__merge(l,mij+1, end)
            self.__inter(l,start,end, mij)
        
    
    
    def __ui_merge(self, cmd_init):
        try:
            cmd = int(cmd_init)
        except ValueError:
            print("Numar invelid!",cmd)
            return
        l = [2,7,5,3,1,9,8,6,0,4]
        self.__merge(l,0,len(l)-1)
        print(l)
    
    
    def __partitie(self, l, st, dr):
        pivot = l[st]
        i = st
        j = dr 
        while i!=j:
            while l[j]>=pivot and i<j:
                j -= 1
            l[i]=l[j]
            while l[i]<=pivot and i<j:
                i+=1
            l[j]=l[i]
        l[i]=pivot
        return i
    
    
    def __quick(self, l, st, dr):
        pos = self.__partitie(l,st,dr)
        if st < pos - 1:
            self.__quick(l, st, pos-1)
        if pos + 1 <dr:
            self.__quick(l, pos +1, dr)
    
    
    def __ui_quick(self, cmd_init):
        try: 
            cmd = int(cmd_init)
        except ValueError:
            print("Numar invelid!",cmd)
            return
        l = [2,7,5,3,1,9,8,6,0,-1]
        self.__quick(l,0,len(l)-1)
        print(l)
    
    
    def run(self):
        while True:
            cmd_init = input("Introdu un numar pe care il doresti descompus in sume de numere prime: ")
            if cmd_init == "exit":
                return
            elif cmd_init == "":
                continue
            else:
                #self.__ui_descompunere(cmd_init)
                self.__ui_merge(cmd_init)
                #self.__ui_quick(cmd_init)
            
