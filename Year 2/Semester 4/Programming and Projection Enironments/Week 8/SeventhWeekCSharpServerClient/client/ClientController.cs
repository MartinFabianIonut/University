using System;
using System.Collections.Generic;
using SeventhWeekCSharpServerClient.domain;
using SeventhWeekCSharpServerClient.services;

namespace SeventhWeekCSharpServerClient.client
{
    public class ClientController : IObserver
    {
        public event EventHandler<EmployeeEventArgs> UpdateEvent;

        public ClientController(IServices service)
        {
            this.Service = service;
            Employee = null;
        }

        public IServices Service { get; }
        public Employee<int> Employee { get; private set; }

        public void Login(string username, string password)
        {
            var tryEmployee = new Employee<int>(1, "", "", username, password);
            var employee = Service.AuthenticateEmployee(tryEmployee, this);
            Console.WriteLine(@"Login succeeded ....");
            Employee = employee;
            Console.WriteLine(@"Current employee {0}", employee);
        }

        public void Logout()
        {
            Console.WriteLine(@"Ctrl logout");
            Service.Logout(Employee);
            Employee = null;
        }

        protected virtual void OnUserEvent(EmployeeEventArgs e)
        {
            if (UpdateEvent == null) return;
            Console.WriteLine(@"Update Event called");
            UpdateEvent(this, e);
        }

        public void TicketAdded(Ticket<int> ticket)
        {
            Console.WriteLine(@"Ticket bought " + ticket);
            var userArgs = new EmployeeEventArgs(EmployeeEvent.TICKET_BOUGHT, ticket);
            OnUserEvent(userArgs);
        }

        public List<ShowDTO<int>> GetAllShowsDTO()
        {
            return Service.GetAllShowsDTO();
        }

        public void AddTickets(Ticket<int> ticket)
        {
            Service.AddTickets(ticket);
        }
    }
}
