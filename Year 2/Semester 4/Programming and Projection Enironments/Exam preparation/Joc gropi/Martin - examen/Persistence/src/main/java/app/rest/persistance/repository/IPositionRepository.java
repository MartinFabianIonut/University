package app.rest.persistance.repository;

import domain.Entity;
import domain.Position;

import java.io.Serializable;

public interface IPositionRepository<ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    ID getMaxId();
    Position findByRowCol(int row, int col);
}
