package app.rest.persistance.repository;

import domain.Entity;
import domain.Score;

import java.io.Serializable;

public interface IScoreRepository<ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    Score getLatestScoreForPlayer(Integer idPlayer);
    ID getMaxId();
}
