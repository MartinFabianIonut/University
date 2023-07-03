using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Saptamana3.domain
{
    internal class Employee<ID> : Entity<ID>
    {
        private string firstName;
        private string lastName;
        private string username;
        private string password;

        public Employee(ID id, string firstName,
            string lastName, string username,
            string password) : base(id) 
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.password = password;
        }

        public string FirstName { 
            get { return this.firstName; } 
            set { this.firstName = value; } 
        }

        public string LastName
        {
            get { return this.lastName; }
            set { this.lastName = value; }
        }

        public string Username
        {
            get { return this.username; }
            set { this.username = value; }
        }

        public string Password
        {
            get { return this.password; }
            set { this.password = value; }
        }

        public override string ToString()
        {
            return firstName + " " + lastName;
        }
    }
}
