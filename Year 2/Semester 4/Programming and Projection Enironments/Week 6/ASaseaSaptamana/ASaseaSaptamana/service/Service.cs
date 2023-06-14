using ASaseaSaptamana.domain;
using ASaseaSaptamana.repository;
using ASaseaSaptamana.repository.interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ASaseaSaptamana.service
{
    public class Service
    {
        private IArtistRepository _artistRepository;
        private IEmployeeRepository _employeeRepository;
        private IShowRepository _showRepository;
        private ITicketRepository _ticketRepository;

        public Service(IArtistRepository artistRepository, IEmployeeRepository employeeRepository, IShowRepository showRepository, ITicketRepository ticketRepository)
        {
            _artistRepository = artistRepository;
            _employeeRepository = employeeRepository;
            _showRepository = showRepository;
            _ticketRepository = ticketRepository;
        }

        public Artist<int> FindArtist(int id) { return _artistRepository.FindById(id); }

        public List<Artist<int>> GetAllArtists() { return _artistRepository.GetAll(); }

        public void AddTicket(Ticket<int> ticket) { _ticketRepository.AddTicket(ticket);
        
        }

        public List<ShowDTO<int>> GetAllShowsDTO()
        {
            return _showRepository.GetAllShows();
        }

        public Employee<int> AuthenticateEmployee(string username, string password)
        {
            return _employeeRepository.AuthenticateEmployee(username, password);
        }
    }
}
