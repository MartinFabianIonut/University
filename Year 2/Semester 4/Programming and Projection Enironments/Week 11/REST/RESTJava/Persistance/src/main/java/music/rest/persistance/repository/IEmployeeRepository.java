package music.rest.persistance.repository;

import rest.domain.Entity;

import java.io.Serializable;

public interface IEmployeeRepository <ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    E authenticateEmployee(String username, String password);
}
