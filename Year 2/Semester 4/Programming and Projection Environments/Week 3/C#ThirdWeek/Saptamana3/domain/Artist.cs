using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Saptamana3.domain
{
    internal class Artist<ID> : Entity<ID>
    {
        private string firstName;
        private string lastName;

        public Artist(ID id, string firstName,
            string lastName) : base(id)
        {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public string FirstName
        {
            get { return this.firstName; }
            set { this.firstName = value; }
        }

        public string LastName
        {
            get { return this.lastName; }
            set { this.lastName = value; }
        }
        public override string ToString()
        {
            return firstName + " " + lastName;
        }
        
    }
}
