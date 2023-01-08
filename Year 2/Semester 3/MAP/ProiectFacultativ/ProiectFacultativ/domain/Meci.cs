namespace ProiectFacultativ.domain
{
    internal class Meci<ID> : Entity<ID>
    {
        private Echipa<ID> echipa1, echipa2;
        private DateTime data;

        public Meci(ID id, Echipa<ID> echipa1, Echipa<ID> echipa2, DateTime data) : base(id)
        {
            this.echipa1 = echipa1;
            this.echipa2 = echipa2;
            this.data = data;
        }

        public Echipa<ID> PrimaEchipa { get { return echipa1; } set { this.echipa1 = value; } }
        public Echipa<ID> ADouaEchipa { get { return echipa2; } set { this.echipa2 = value; } }
        public DateTime Data { get { return data; } set { this.data = value; } }

        public override string ToString()
        {
            return base.ToString() + echipa1.ToString() + " vs. " + echipa2.ToString() + "\t din data: " + data.ToString() ;
        }
    }
}
