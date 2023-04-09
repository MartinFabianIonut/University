package SeventhWeekServerClient.persistance.repository;

import SeventhWeekServerClient.domain.Entity;
import SeventhWeekServerClient.domain.ShowDTO;

public interface IShowRepository <ID, E extends Entity<ID>> extends IRepository<ID, E> {
    Iterable<ShowDTO> getAllShowsDTO();
}
