package com.example.temasaptamana5.repository;

import com.example.temasaptamana5.domain.Entity;

public interface IRepository<ID, E extends Entity<ID>>  {
    /**
     *
     * @param id of type ID (template)
     * @return found entity or null
     */
    E findById(ID id);
    Iterable<E> getAll();
    boolean add(E entity);
    boolean delete(ID id);
    boolean update(E entity);
}
