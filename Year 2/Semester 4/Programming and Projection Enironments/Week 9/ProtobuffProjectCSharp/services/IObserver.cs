using ProtobuffProject.domain;

namespace ProtobuffProject.services
{
    public interface IObserver
    {
        void TicketAdded(Ticket<int> ticket);
    }
}
