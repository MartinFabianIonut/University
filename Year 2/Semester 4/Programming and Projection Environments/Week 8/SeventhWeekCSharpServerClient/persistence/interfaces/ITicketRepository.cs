using SeventhWeekCSharpServerClient.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SeventhWeekCSharpServerClient.repository.interfaces
{
    public interface ITicketRepository : IRepository<int, Ticket<int>>
    {
        void AddTicket(Ticket<int> ticket);
    }
}
