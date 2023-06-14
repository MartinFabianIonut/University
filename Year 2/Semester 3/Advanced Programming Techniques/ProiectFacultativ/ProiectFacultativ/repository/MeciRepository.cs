using ProiectFacultativ.domain;

namespace ProiectFacultativ.repository
{
    internal class MeciRepository<ID> : AbstractRepository<ID, Meci<ID>>
    {
        private EchipaRepository<ID> echipaRepository;
        public MeciRepository(string filePath, EchipaRepository<ID> echipaRepository) : base(filePath)
        {
            this.echipaRepository = echipaRepository;
            base.LoadData(filePath);
        }
        /*
         * Extrage o entitate de tipul Meci pe baza unui input de tip string
         */
        public override Meci<ID> ExtractEntity(string[] values)
        {
            ID id = (ID)Convert.ChangeType(values[0], typeof(ID));
            Echipa<ID> echipa1 = echipaRepository.findByNume(values[1]);
            Echipa<ID> echipa2 = echipaRepository.findByNume(values[2]);
            Meci<ID> meci = new(id, echipa1, echipa2, DateTime.Parse(values[3]));
            return meci;
        }
    }
}
