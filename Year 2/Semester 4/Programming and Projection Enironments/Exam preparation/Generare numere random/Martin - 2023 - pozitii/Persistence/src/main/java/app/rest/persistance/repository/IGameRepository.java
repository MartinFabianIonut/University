package app.rest.persistance.repository;

import domain.Entity;

import java.io.Serializable;

public interface IGameRepository <ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    ID getMaxId();
}
