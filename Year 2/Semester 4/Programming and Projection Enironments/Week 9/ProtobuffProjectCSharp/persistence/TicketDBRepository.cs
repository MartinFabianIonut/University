using log4net;
using ProtobuffProject.domain;
using ProtobuffProject.repository.interfaces;
using System.Collections.Generic;
using System.Data.SQLite;

namespace ProtobuffProject.repository
{
    public class TicketDBRepository : ITicketRepository
    {
        private static readonly ILog Logger = LogManager.GetLogger("TicketDBRepository");

        private readonly IDictionary<string, string> props;

        public TicketDBRepository(IDictionary<string, string> props)
        {
            this.props = props;
            Logger.Info("Creating TicketDBRepository");
        }

        private static Ticket<int> GetTicketFromResultSet(SQLiteDataReader resultSet)
        {
            var id = resultSet.GetInt32(0);
            var idShow = resultSet.GetInt32(1);
            var nameOfCostumer = resultSet.GetString(2);
            return new Ticket<int>(id, idShow, nameOfCostumer, 1);
        }

        public List<Ticket<int>> GetAll()
        {
            Logger.Info("finding all tickets");
            var connection = DBUtils.GetConnection(props);
            var list = new List<Ticket<int>>();
            using (var cmd = new SQLiteCommand("select * from ticket", connection as SQLiteConnection))
            using (var reader = cmd.ExecuteReader())
                while (reader.Read())
                    list.Add(GetTicketFromResultSet(reader));
            Logger.Info(list);
            return list;
        }


        public Ticket<int> FindById(int id)
        {
            Logger.Info("find a ticket by id");
            var connection = DBUtils.GetConnection(props);
            var cmd = new SQLiteCommand("select * from ticket where id = " + id,
                connection as SQLiteConnection);
            using (var reader = cmd.ExecuteReader())
            {
                if (!reader.Read()) return null;
                var ticket = GetTicketFromResultSet(reader);
                Logger.Info(ticket);
                return ticket;
            }
        }

        public bool Add(Ticket<int> ticket)
        {
            Logger.Info("adding ticket");
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand
                ("insert into ticket values (@id, @id_show, @name_of_costumer)",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", null);
                command.Parameters.AddWithValue("@id_show", ticket.IdShow);
                command.Parameters.AddWithValue("@name_of_costumer", ticket.NameOfCostumer);
                command.ExecuteNonQuery();
                Logger.InfoFormat("inserted successfully {0}", ticket);
                return true;
            }
        }

        public void AddTicket(Ticket<int> ticket)
        {
            Logger.Info("adding ticket to available seat");
            var connection = DBUtils.GetConnection(props);
            var noOfSeats = ticket.NoOfSeats;
            for (var i = 0; i < noOfSeats; i++)
            {
                using (var command = new SQLiteCommand
                       ("select id from ticket where name_of_costumer is null and id_show = @id_show",
                           connection as SQLiteConnection))
                {
                    command.Parameters.AddWithValue("@id_show", ticket.IdShow);
                    using (var reader = command.ExecuteReader())
                        if (reader.Read())
                            using (var command2 = new SQLiteCommand
                                   ("update ticket set id_show = @id_show, name_of_costumer = @name_of_costumer where id = @id",
                                       connection as SQLiteConnection))
                            {
                                command2.Parameters.AddWithValue("@id", reader.GetInt32(0));
                                command2.Parameters.AddWithValue("@id_show", ticket.IdShow);
                                command2.Parameters.AddWithValue("@name_of_costumer", ticket.NameOfCostumer);
                                command2.ExecuteNonQuery();
                                Logger.InfoFormat("ticket bought successfully {0}", ticket);
                            }

                }
            }
        }

        public bool Delete(int id)
        {
            Logger.InfoFormat("delete ticket with id = {0}", id);
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand("delete from ticket where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", id);
                command.ExecuteNonQuery();
                Logger.InfoFormat("deleted successfully for id {0}", id);
                return true;
            }
        }

        public bool Update(Ticket<int> ticket)
        {
            Logger.Info("updating employee");
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand("update ticket set id_show = @id_show, " +
                "name_of_costumer = @name_of_costumer where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id_show", ticket.IdShow);
                command.Parameters.AddWithValue("@name_of_costumer", ticket.NameOfCostumer);
                command.Parameters.AddWithValue("@id", ticket.Id);
                command.ExecuteNonQuery();
                Logger.Info("updated successfully");
                return true;
            }
        }
    }
}