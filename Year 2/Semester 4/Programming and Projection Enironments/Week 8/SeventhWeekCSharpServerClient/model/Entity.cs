using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SeventhWeekCSharpServerClient.domain
{
    [Serializable]
    public class Entity<ID>
    {
        private ID id;
        public Entity(ID id) { this.id = id; }
        public ID Id
        {
            get => this.id;
            set => this.id = value;
        }
        public override string ToString()
        {
            return id.ToString() + " ";
        }
    }
}
