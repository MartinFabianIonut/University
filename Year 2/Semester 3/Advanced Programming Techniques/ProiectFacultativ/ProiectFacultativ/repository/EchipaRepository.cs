using ProiectFacultativ.domain;

namespace ProiectFacultativ.repository
{
    internal class EchipaRepository<ID> : AbstractRepository<ID, Echipa<ID>>
    {
        public EchipaRepository(string filePath) : base(filePath)
        {
            base.LoadData(filePath);
        }
        /*
         * Extrage o entitate de tipul Echipa pe baza unui input de tip string
         */
        public override Echipa<ID> ExtractEntity(string[] values)
        {
            ID id = (ID)Convert.ChangeType(values[0], typeof(ID));
            Echipa<ID> echipa = new(id, values[1]);
            return echipa;
        }

        public Echipa<ID> findByNume(string nume) {
            return entities.Find(x => x.Numele.Equals(nume));
        }
    }
}
