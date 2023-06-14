package music.rest.persistance.repository;
import rest.domain.Entity;

import java.io.Serializable;

public interface IArtistRepository <ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    ID getMaxId();
}
