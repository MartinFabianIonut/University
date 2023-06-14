using ProiectFacultativ.domain;

namespace ProiectFacultativ.repository
{
    internal class JucatorActivRepository<ID> : AbstractRepository<ID, JucatorActiv<ID>>
    {
        public JucatorActivRepository(string filePath) : base(filePath){ base.LoadData(filePath); }
        /*
         * Extrage o entitate de tipul JucatorActiv pe baza unui input de tip string
         */
        public override JucatorActiv<ID> ExtractEntity(string[] values)
        {
            ID id = (ID)Convert.ChangeType(values[0], typeof(ID));
            ID idJucator = (ID)Convert.ChangeType(values[1], typeof(ID));
            ID idMeci = (ID)Convert.ChangeType(values[2], typeof(ID));
            int puncte = Convert.ToInt32(values[3]);
            string tip = Convert.ToString(values[4]);
            JucatorActiv<ID> jucatorActiv = new(id, idJucator, idMeci, puncte, tip);
            return jucatorActiv;
        }
    }
}
