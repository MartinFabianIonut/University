using SeventhWeekCSharpServerClient.domain;
using System.Collections.Generic;

namespace SeventhWeekCSharpServerClient.services
{
    public interface IServices
    {
        void AddTickets(Ticket<int> ticket);
        Employee<int> AuthenticateEmployee(Employee<int> employee, IObserver employeeObserver);
        void Logout(Employee<int> employee);
        List<ShowDTO<int>> GetAllShowsDTO();
        List<Artist<int>> GetAllArtists();
        Artist<int> FindArtist(int id);

    }
}
