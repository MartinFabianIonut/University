package SeventhWeekServerClient.persistance.repository;
import SeventhWeekServerClient.domain.Entity;

public interface IArtistRepository <ID, E extends Entity<ID>> extends IRepository<ID, E> {
}
