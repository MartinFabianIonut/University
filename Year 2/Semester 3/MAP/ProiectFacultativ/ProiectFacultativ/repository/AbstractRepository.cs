using ProiectFacultativ.domain;

namespace ProiectFacultativ.repository
{
    internal abstract class AbstractRepository<ID, E> : IRepository<ID, E> where E : Entity<ID>
    {
        /*
         * Metodă abstractă pentru obținerea tuturor entităților de tip E (Echipa/Jucator/JucatorActiv/Meci), 
         * metodă care este implementată atât în EchipaRepository, cât și în JucatorRepository, 
         * JucatorActivRepository si MeciRepository
         * 
         * @return o entitate de tipul generic E
         */
        public abstract E ExtractEntity(string[] values);

        private string filePath;
        protected List<E> entities = new List<E>();

        public AbstractRepository(string filePath) {
            this.filePath = filePath;
        }

        /*
         * Încarcă date prin extragerea tuturor entităților de tip E (Echipa/Jucator/JucatorActiv/Meci) - genericitate, 
         * prin apelarea metodei abstracte extractEntity(), care este implementată atât în 
         * EchipaRepository, cât și în JucatorRepository, JucatorActivRepository si MeciRepository
         */
        protected void LoadData(string filePath) {
            entities.Clear();
            using (StreamReader reader = new StreamReader(filePath))
            {
                while (!reader.EndOfStream)
                {
                    string line = reader.ReadLine();
                    string[] values = line.Split(',');
                    E entity = ExtractEntity(values);
                    entities.Add(entity);
                }
            }
        }
        /*
         * Functie care incarca toate elementele din fisier si returneaza o colectie iterabila de elemente de tipul E
         */
        public IEnumerable<E> GetAll() {
            LoadData(filePath);
            return entities;
        }

        /*
         * Functie care cauta si returneaza un obiect de tipul E din lista obiectelor de acel tip, dupa ID
         */
        public E FindOne(ID id) {
            return entities.Find(entity => entity.Id.Equals(id));
        }

    }
}
