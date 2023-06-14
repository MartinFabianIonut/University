using ProiectFacultativ.domain;

namespace ProiectFacultativ.repository
{
    internal class JucatorRepository<ID> : AbstractRepository<ID, Jucator<ID>>
    {
        private EchipaRepository<ID> echipaRepository;
        public JucatorRepository(string filePath, EchipaRepository<ID> echipaRepository) : base(filePath) 
        {
            this.echipaRepository = echipaRepository;
            base.LoadData(filePath);
        }
        /*
         * Extrage o entitate de tipul Jucator pe baza unui input de tip string
         */
        public override Jucator<ID> ExtractEntity(string[] values)
        {
            ID id = (ID)Convert.ChangeType(values[0], typeof(ID));
            Echipa<ID> echipa = echipaRepository.findByNume(values[2]);
            Jucator<ID> jucator = new(id, values[1], echipa, values[3]);
            return jucator;
        }
    }
}
