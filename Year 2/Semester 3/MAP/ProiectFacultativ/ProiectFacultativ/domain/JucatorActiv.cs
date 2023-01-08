namespace ProiectFacultativ.domain
{
    internal class JucatorActiv<ID> : Entity<ID>
    {
        private ID idJucator, idMeci;
        private int nrPuncteInscrise;
        private string tip;

        public JucatorActiv(ID id, ID idJucator, ID idMeci, int nrPuncteInscrise, string tip) : base(id)
        {
            this.idJucator = idJucator;
            this.idMeci = idMeci;
            this.nrPuncteInscrise = nrPuncteInscrise;
            this.tip = tip;
        }

        public ID IdJucator { get { return this.idJucator; } set { this.idJucator = value; } }
        public ID IdMeci { get { return this.idMeci; } set { this.idMeci = value; } }
        public int NrPuncteInscrise { get { return this.nrPuncteInscrise; } set { this.nrPuncteInscrise = value; } }
        public string Tip { get { return this.tip; } set { this.tip = value; } }

        public override string ToString()
        {
            return base.ToString() + "Jucator: " + idJucator.ToString() + "Meci: " + 
                idMeci.ToString() + "Nr puncte inscrise: " + nrPuncteInscrise.ToString() + "Tip: " + tip;
        }
    }
}
