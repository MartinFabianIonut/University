using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SaptamanaAPatra.domain
{
    internal class Ticket<ID> : Entity<ID>
    {
        private int idShow;
        private string nameOfCostumer;

        public Ticket(ID id, int idShow, string nameOfCostumer)
        : base(id) {
            this.idShow = idShow;
            this.nameOfCostumer = nameOfCostumer;
        }

        public int IdShow
        {
            get { return this.idShow; }
            set { this.idShow = value; }
        }
        
        public string NameOfCostumer
        {
            get { return this.nameOfCostumer; }
            set { this.nameOfCostumer = value; }
        }

    }
}
