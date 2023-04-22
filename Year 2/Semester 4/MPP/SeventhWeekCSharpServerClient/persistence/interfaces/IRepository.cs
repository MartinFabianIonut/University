using SeventhWeekCSharpServerClient.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SeventhWeekCSharpServerClient.repository.interfaces
{
    public interface IRepository<ID, E> where E : Entity<ID>
    {
        List<E> GetAll();
        E FindById(ID id);
        bool Add(E entity);
        bool Delete(ID id);
        bool Update(E entity);
    }
}
