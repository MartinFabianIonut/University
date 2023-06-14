namespace ProiectFacultativ.domain
{
    internal class Echipa<ID> : Entity<ID>
    {
        private string nume;
        public string Numele { get { return nume; } set { nume = value; } }

        public Echipa(ID id, string nume) : base(id) { this.nume = nume; }

        public override string ToString()
        {
            return nume;
        }
    }
}
