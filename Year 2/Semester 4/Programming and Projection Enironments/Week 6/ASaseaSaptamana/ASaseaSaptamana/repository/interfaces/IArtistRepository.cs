using ASaseaSaptamana.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ASaseaSaptamana.repository.interfaces
{
    public interface IArtistRepository : IRepository<int, Artist<int>>
    {
    }
}
