using SeventhWeekCSharpServerClient.domain;

namespace SeventhWeekCSharpServerClient.services
{
    public interface IObserver
    {
        void TicketAdded(Ticket<int> ticket);
    }
}
