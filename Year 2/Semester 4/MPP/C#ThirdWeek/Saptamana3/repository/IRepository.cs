using Saptamana3.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Saptamana3.repository
{
    internal interface IRepository<ID, E> where E : Entity<ID>
    {
        IEnumerable<E> GetAll();
        E FindById(ID id);
        bool Add(E entity);
        E Delete(ID id);
        E Update(E entity);
    }
}
