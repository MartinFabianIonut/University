using ProtobuffProject.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProtobuffProject.domain
{
    [Serializable]
    public class ShowDTO<ID> : Entity<ID>
    {
        private int available;
        private int unavailable;

        public ShowDTO(ID id, string title, string date, string place, int available, int unavailable, int idArtist)
        :base(id)
        {
            this.Title = title;
            this.Date = date;
            this.Place = place;
            this.available = available;
            this.unavailable = unavailable;
            this.IdArtist = idArtist;
        }

        public string Title { get; set;}
        public string Date { get; set; }
        public string Place { get; set; }

        public int Available
        {
            get => available;
            set => available = value;
        }

        public int Unavailable
        {
            get => unavailable;
            set => unavailable = value;
        }

        public int IdArtist { get; set; }

        public override string ToString()
        {
            return base.ToString() + " " + Title + " " + Date + " " + Place + " " + available + " " + unavailable + " " + IdArtist;
        }
    }
}
