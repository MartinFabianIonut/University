package music.rest.persistance.repository;

import rest.domain.Entity;
import rest.domain.ShowDTO;

import java.io.Serializable;

public interface IShowRepository <ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    Iterable<ShowDTO> getAllShowsDTO();
}
