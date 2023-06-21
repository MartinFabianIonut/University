package app.rest.persistance.repository;

import domain.Entity;

import java.io.Serializable;

public interface IRepository<ID extends Serializable, E extends Entity<ID>>  {
    /**
     *
     * @param id of type ID (template)
     * @return found entity or null
     */
    E findById(ID id);
    Iterable<E> getAll();
    boolean add(E entity);

    boolean update(E entity);
}
