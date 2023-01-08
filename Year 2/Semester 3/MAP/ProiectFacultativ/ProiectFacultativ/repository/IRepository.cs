using ProiectFacultativ.domain;

namespace ProiectFacultativ.repository
{
    internal interface IRepository<ID, E> where E : Entity<ID>
    {
        IEnumerable<E> GetAll();
    }
}
