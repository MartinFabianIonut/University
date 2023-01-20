class Sorter(object):
    
    def _identitate(self, obiect):
        return obiect
    
    def _negatie(self, obiect):
        return not obiect
    
    def sort(self, lista,*, key = lambda x: x, cmp = lambda x,y : x<y, reverse = False):
        pass


class InsertSort(Sorter):
    

    def __insertion_sort(self, lista, key, cmp, operatie):
        for poz in range(1,len(lista)):
            indice = poz-1
            obiect = lista[poz]
            #insert a in the right position
            while indice >= 0 and operatie(cmp(key(obiect),key(lista[indice]))):
                lista[indice+1] = lista[indice]
                indice = indice-1
            lista[indice+1] = obiect
    
    
    
    def sort(self, lista, *,key=lambda x:x, cmp=lambda x, y:x < y, reverse=False):
        if reverse:
            operatie = self._negatie
        else:
            operatie = self._identitate
        self.__insertion_sort(lista, key, cmp, operatie)
        
        
        
class MergeSort(Sorter):
    

    def __merge(self, lista, key, cmp, operatie, start, end, mij):
        i = start
        j = mij + 1
        k = start
        aux = [None]*len(lista)
        while i<= mij and j <= end:
            if operatie(cmp(key(lista[i]),key(lista[j]))):
                aux[k] = lista[i]
                k+=1
                i+=1
            else:
                aux[k]=lista[j]
                k+=1
                j+=1
        while i<=mij:
            aux[k] = lista[i]
            k+=1
            i+=1
        while j<=end:
            aux[k]=lista[j]
            k+=1
            j+=1
        for i in range(start,end+1):
            lista[i]=aux[i] 
    
    
    def __merge_sort(self, lista, key, cmp, operatie, start, end):
        if start < end:
            mij = (start+end)//2
            self.__merge_sort(lista, key, cmp, operatie, start, mij)
            self.__merge_sort(lista, key, cmp, operatie, mij + 1, end)
            self.__merge(lista, key, cmp, operatie, start, end, mij)
    
    
    
    def sort(self, lista, *,key=lambda x:x, cmp=lambda x, y:x < y, reverse=False):
        if reverse:
            operatie = self._negatie
        else:
            operatie = self._identitate
        start = 0
        end = len(lista)-1
        self.__merge_sort(lista, key, cmp, operatie, start, end)



class CombSort(Sorter):
    
    def __getNextGap(self, gap):
        # Shrink gap by Shrink factor
        gap = (gap * 10)//13
        if gap < 1:
            return 1
        return gap
 
    # Functie de sortare a unei liste - Comb Sort
    def __combSort(self, lista, key, cmp, operatie):
        n = len(lista)
        # Se initializeaza gap
        gap = n
        swapped = True
     
        # Continua cat timp gap > 1 si cat simp s-a efectuat o schimbare
        while gap != 1 or swapped == True:
            # Alfa urmatorul gap
            gap = self.__getNextGap(gap)
     
            # Presupunem ca nu se face nicio schimbare
            swapped = False
     
            # Compara toate elementele cu gap-ul actual
            for i in range(0, n-gap):
                if operatie(cmp(key(lista[i + gap]),key(lista[i]))):
                    lista[i], lista[i + gap] = lista[i + gap], lista[i]
                    swapped = True
                
            
    def sort(self, lista, key=lambda x:x, cmp=lambda x, y : x < y, reverse=False):
        if reverse:
            operatie = self._negatie
        else:
            operatie = self._identitate
        self.__combSort(lista, key, cmp, operatie)
    


