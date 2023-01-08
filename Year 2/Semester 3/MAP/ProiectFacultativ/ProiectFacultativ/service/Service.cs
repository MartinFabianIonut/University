using ProiectFacultativ.domain;
using ProiectFacultativ.repository;

namespace ProiectFacultativ.service
{
    internal class Service<ID>
    {
        private readonly MeciRepository<ID> meciRepository;
        private readonly JucatorRepository<ID> jucatorRepository;
        private readonly EchipaRepository<ID> echipaRepository;
        private readonly JucatorActivRepository<ID> jucatorActivRepository;

        public Service(MeciRepository<ID> meciRepository, JucatorRepository<ID> jucatorRepository, EchipaRepository<ID> echipaRepository, JucatorActivRepository<ID> jucatorActivRepository)
        {
            this.meciRepository = meciRepository;
            this.jucatorRepository = jucatorRepository;
            this.echipaRepository = echipaRepository;
            this.jucatorActivRepository = jucatorActivRepository;
        }

        public List<Jucator<ID>> GetJucatoriEchipaData(string numeEchipa)
        {
            List<Jucator<ID>> jucatori = (List<Jucator<ID>>)jucatorRepository.GetAll();
            IEnumerable<Jucator<ID>> jucatoriEchipaData = jucatori.Where(x => x.Echipa.Numele.Equals(numeEchipa));
            return jucatoriEchipaData.ToList();
        }

        public Jucator<ID> FindJucator(ID id)
        {
            return jucatorRepository.FindOne(id);
        }

        public List<JucatorActiv<ID>> GetJucatoriActiviPeEchipaLaMeci(string numeEchipa, ID idMeci)
        {
            List<JucatorActiv<ID>> jucatoriActivi = (List<JucatorActiv<ID>>)jucatorActivRepository.GetAll();
            List<JucatorActiv<ID>> jucatoriPeEchipaLaMeci = new();
            foreach (var jucator in jucatoriActivi)
            {
                if (jucator.IdMeci.Equals(idMeci) && 
                    jucatorRepository.FindOne(jucator.IdJucator).Echipa.Numele.Equals(numeEchipa)
                    && jucator.Tip.Equals("Participant"))
                    jucatoriPeEchipaLaMeci.Add(jucator);
            }
            return jucatoriPeEchipaLaMeci;
        }

        public List<Meci<ID>> GetMeciuriInPerioada(string data1, string data2)
        {
            DateTime start = DateTime.Parse(data1);
            DateTime end = DateTime.Parse(data2);
            List<Meci<ID>> meciuri = (List<Meci<ID>>)meciRepository.GetAll();
            List<Meci<ID>> meciuriInPerioada = new();
            meciuriInPerioada = meciuri.FindAll(meci => start.CompareTo(meci.Data) <= 0 && end.CompareTo(meci.Data) >= 0);
            return meciuriInPerioada;
        }

        public string GetScorLaMeci(ID idMeci)
        {
            Meci<ID> meci = meciRepository.FindOne(idMeci);
            if (meci == null)
                return "Nu exista meci cu acest id!";
            List<JucatorActiv<ID>> jucatoriActivi = (List<JucatorActiv<ID>>)jucatorActivRepository.GetAll();
            List<JucatorActiv<ID>> jucatoriInMeci = new();
            jucatoriInMeci = jucatoriActivi.FindAll(jucator => jucator.IdMeci.Equals(idMeci));
            Dictionary<string, int> scor = new()
            {
                { meci.PrimaEchipa.Numele, 0 },
                { meci.ADouaEchipa.Numele, 0 }
            };
            foreach (JucatorActiv<ID> jucator in jucatoriInMeci)
            {
                string numeEchipa = jucatorRepository.FindOne(jucator.IdJucator).Echipa.Numele;
                scor[numeEchipa] += jucator.NrPuncteInscrise;
            }
            string rezultat = "Meciul intre: " + scor.Keys.ToArray()[0] + " si " + scor.Keys.ToArray()[1] + " => ";
            rezultat += scor[scor.Keys.ToArray()[0]].ToString() + "-" + scor[scor.Keys.ToArray()[1]].ToString();
            return rezultat;
        }

        public List<Meci<ID>> GetMeciuri()
        {
            return (List<Meci<ID>>)meciRepository.GetAll();
        }
        public List<Jucator<ID>> GetJucatori()
        {
            return (List<Jucator<ID>>)jucatorRepository.GetAll();
        }public List<Echipa<ID>> GetEchipe()
        {
            return (List<Echipa<ID>>)echipaRepository.GetAll();
        }
        public List<JucatorActiv<ID>> GetJucatoriActivi()
        {
            return (List<JucatorActiv<ID>>)jucatorActivRepository.GetAll();
        }
    }
}
