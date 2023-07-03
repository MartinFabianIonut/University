using SaptamanaAPatra.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SaptamanaAPatra.repository.interfaces
{
    internal interface ITicketRepository : IRepository<int, Ticket<int>>
    {
    }
}
