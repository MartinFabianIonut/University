using SaptamanaAPatra.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SaptamanaAPatra.repository.interfaces
{
    internal interface IRepository<ID, E> where E : Entity<ID>
    {
        List<E> GetAll();
        E FindById(ID id);
        bool Add(E entity);
        bool Delete(ID id);
        bool Update(E entity);
    }
}
