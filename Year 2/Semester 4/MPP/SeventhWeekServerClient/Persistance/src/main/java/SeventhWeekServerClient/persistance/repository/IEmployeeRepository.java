package SeventhWeekServerClient.persistance.repository;

import SeventhWeekServerClient.domain.Entity;
public interface IEmployeeRepository <ID, E extends Entity<ID>> extends IRepository<ID, E> {
    E authenticateEmployee(String username, String password);
}
