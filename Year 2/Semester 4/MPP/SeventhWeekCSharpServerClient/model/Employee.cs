using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SeventhWeekCSharpServerClient.domain
{
    [Serializable]
    public class Employee<ID> : Entity<ID>
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
            get => this.firstName;
            set => this.firstName = value;
        }

        public string LastName
        {
            get => this.lastName;
            set => this.lastName = value;
        }

        public string Username
        {
            get => this.username;
            set => this.username = value;
        }

        public string Password
        {
            get => this.password;
            set => this.password = value;
        }

        public override string ToString()
        {
            return firstName + " " + lastName;
        }
    }
}
