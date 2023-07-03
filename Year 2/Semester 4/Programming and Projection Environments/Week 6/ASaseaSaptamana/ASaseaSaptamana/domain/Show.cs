using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ASaseaSaptamana.domain
{
    public class Show<ID>:Entity<ID>
    {
        private string title;
        private string date;
        private string place;
        private int idArtist;

        public Show(ID id, string title, string date, string place, 
            int idArtist) : base(id)
        {
            this.title = title;
            this.date = date;
            this.place = place;
            this.idArtist = idArtist;
        }

        public string Title
        {
            get { return this.title; }
            set { this.title = value; }
        }

        public string Date
        {
            get { return this.date; }
            set { this.date = value; }
        }

        public string Place
        {
            get { return this.place; }
            set { this.place = value; }
        }

        public int IdArtist
        {
            get { return this.idArtist; }
            set { this.idArtist = value; }
        }

        public override string ToString()
        {
            return "The show '" + title + "' will take place on " + date + ", at " + place;
        }
    }
}
