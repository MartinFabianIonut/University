package gRPCProject.persistance.repository;

import gRPCProject.domain.Entity;
import gRPCProject.domain.ShowDTO;

import java.io.Serializable;

public interface IShowRepository <ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    Iterable<ShowDTO> getAllShowsDTO();
}
