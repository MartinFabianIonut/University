using SeventhWeekCSharpServerClient.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SeventhWeekCSharpServerClient.repository.interfaces
{
    public interface IArtistRepository : IRepository<int, Artist<int>>
    {
    }
}
