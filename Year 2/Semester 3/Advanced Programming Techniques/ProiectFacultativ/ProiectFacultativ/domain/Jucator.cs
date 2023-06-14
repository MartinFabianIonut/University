namespace ProiectFacultativ.domain
{
    internal class Jucator<ID>: Elev<ID>
    {
        private Echipa<ID> echipa;

        public Jucator(ID id, string nume, Echipa<ID> echipa, string scoala) : base(id, nume, scoala)
        {
            this.echipa= echipa;
        }

        public Echipa<ID> Echipa { get { return this.echipa; } set { this.echipa = value; } }

        public override string ToString()
        {
            return base.ToString() + ", din echipa: " + echipa.Numele;
        }
    }
}
