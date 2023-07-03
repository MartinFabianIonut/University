using ASaseaSaptamana.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ASaseaSaptamana.domain
{
    public class ShowDTO<ID> : Entity<ID>
    {
        private string title;
        private string date;
        private string place;
        private int available;
        private int unavailable;
        private int idArtist;

        public ShowDTO(ID id, string title, string date, string place, int available, int unavailable, int idArtist)
        :base(id)
        {
            this.title = title;
            this.date = date;
            this.place = place;
            this.available = available;
            this.unavailable = unavailable;
            this.idArtist = idArtist;
        }

        public string Title { get { return title; } }
        public string Date { get { return date; } }
        public string Place { get { return place; } }
        public int Available { get { return available;} }
        public int Unavailable { get {  return unavailable;} }
        public int IDArtist { get {  return idArtist; } }
    }
}
