package app.rest.persistance.repository;

import domain.Entity;

import java.io.Serializable;

public interface IPlayerRepository<ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    E login(String alias);
}
