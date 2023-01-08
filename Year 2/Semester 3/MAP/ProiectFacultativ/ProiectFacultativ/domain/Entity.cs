namespace ProiectFacultativ.domain
{
    internal class Entity<ID>
    {
        private ID id;
        public Entity(ID id) { this.id = id; }
        public ID Id { 
            get { return this.id; } 
            set { this.id = value; }
        }

        public object Nume { get; internal set; }

        public override string ToString()
        {
            return id.ToString()+" ";
        }
    }
}
