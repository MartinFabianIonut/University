using SeventhWeekCSharpServerClient.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SeventhWeekCSharpServerClient.domain
{
    [Serializable]
    public class ShowDTO<ID> : Entity<ID>
    {
        private int available;
        private int unavailable;
        private int idArtist;

        public ShowDTO(ID id, string title, string date, string place, int available, int unavailable, int idArtist)
        :base(id)
        {
            this.Title = title;
            this.Date = date;
            this.Place = place;
            this.available = available;
            this.unavailable = unavailable;
            this.idArtist = idArtist;
        }

        public string Title { get; }
        public string Date { get; }
        public string Place { get; }

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

        public int IDArtist => idArtist;

        public override string ToString()
        {
            return base.ToString() + " " + Title + " " + Date + " " + Place + " " + available + " " + unavailable + " " + idArtist;
        }
    }
}
