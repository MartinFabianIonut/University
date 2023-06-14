using System;

namespace ProtobuffProject.domain
{
    [Serializable]
    public class Ticket<ID> : Entity<ID>
    {
        private int idShow;
        private string nameOfCostumer;
        private int noOfSeats;

        public Ticket(ID id, int idShow, string nameOfCostumer, int noOfSeats)
        : base(id) {
            this.idShow = idShow;
            this.nameOfCostumer = nameOfCostumer;
            this.noOfSeats = noOfSeats;
        }

        public int IdShow
        {
            get => this.idShow;
            set => this.idShow = value;
        }
        
        public string NameOfCostumer
        {
            get => this.nameOfCostumer;
            set => this.nameOfCostumer = value;
        }
        
        public int NoOfSeats
        {
            get => this.noOfSeats;
            set => this.noOfSeats = value;
        }

        public override string ToString()
        {
            return base.ToString() + " " + idShow + " " + nameOfCostumer + " noOfSeats: " + noOfSeats;
        }
    }
}
