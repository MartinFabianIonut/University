using System.Collections.Generic;
using System.Threading.Tasks;
using ProtobuffProject.domain;
using ProtobuffProject.repository.interfaces;
using ProtobuffProject.services;

namespace ProtobuffProject.server
{
    public class ServicesImpl : IServices
    {
        private readonly IArtistRepository artistRepository;
        private readonly IEmployeeRepository employeeRepository;
        private readonly IDictionary<int, IObserver> loggedEmployees;
        private readonly IShowRepository showRepository;
        private readonly ITicketRepository ticketRepository;

        public ServicesImpl(IArtistRepository artistRepository, IEmployeeRepository employeeRepository,
            IShowRepository showRepository, ITicketRepository ticketRepository)
        {
            this.artistRepository = artistRepository;
            this.employeeRepository = employeeRepository;
            this.showRepository = showRepository;
            this.ticketRepository = ticketRepository;
            loggedEmployees = new Dictionary<int, IObserver>();
        }

        public void AddTickets(Ticket<int> ticket)
        {
            ticketRepository.AddTicket(ticket);
            foreach (var employeeO in loggedEmployees.Values) Task.Run(() => employeeO.TicketAdded(ticket));
        }

        public Employee<int> AuthenticateEmployee(Employee<int> employee, IObserver employeeObserver)
        {
            var authenticateEmployee = employeeRepository.AuthenticateEmployee(employee.Username, employee.Password);
            if (authenticateEmployee != null)
            {
                if (loggedEmployees.ContainsKey(authenticateEmployee.Id))
                    throw new MyException("User already logged in.");
                loggedEmployees[authenticateEmployee.Id] = employeeObserver;
            }
            else
            {
                throw new MyException("Authentication failed.");
            }

            return authenticateEmployee;
        }

        public void Logout(Employee<int> employee)
        {
            var localClient = loggedEmployees[employee.Id];
            if (localClient == null)
                throw new MyException("User " + employee.Id + " is not logged in.");
            loggedEmployees.Remove(employee.Id);
        }

        public List<ShowDTO<int>> GetAllShowsDTO()
        {
            return showRepository.GetAllShows();
        }

        public List<Artist<int>> GetAllArtists()
        {
            return artistRepository.GetAll();
        }

        public Artist<int> FindArtist(int id)
        {
            return artistRepository.FindById(id);
        }
    }
}