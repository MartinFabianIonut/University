using ProtobuffProject.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProtobuffProject.repository.interfaces
{
    public interface IShowRepository : IRepository<int, Show<int>>
    {
        List<ShowDTO<int>> GetAllShows();
    }
}
