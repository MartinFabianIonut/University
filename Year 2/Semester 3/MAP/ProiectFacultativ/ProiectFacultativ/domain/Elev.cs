namespace ProiectFacultativ.domain
{
    internal class Elev<ID> : Entity<ID>
    {
        private string nume, scoala;

        public Elev(ID id, string nume, string scoala) : base (id)
        {
            this.nume = nume;
            this.scoala = scoala;
        }

        public string Nume { get { return nume; } set { nume = value; } }
        public string Scoala { get { return scoala; } set { scoala = value; } }

        public override string ToString()
        {
            return base.ToString() + nume + ", elev la " + scoala;
        }
    }
}
