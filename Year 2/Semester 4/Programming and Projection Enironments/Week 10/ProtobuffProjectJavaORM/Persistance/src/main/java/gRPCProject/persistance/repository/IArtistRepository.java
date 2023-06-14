package gRPCProject.persistance.repository;
import gRPCProject.domain.Entity;

import java.io.Serializable;

public interface IArtistRepository <ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
}
