using log4net;
using SaptamanaAPatra.domain;
using SaptamanaAPatra.repository.interfaces;
using SaptamanaAPatra.utils;
using System.Collections.Generic;
using System.Data.SQLite;

namespace SaptamanaAPatra.repository
{
    internal class TicketDBRepository : ITicketRepository
    {
        private static readonly ILog logger = LogManager.GetLogger("TicketDBRepository");

        private IDictionary<string, string> _props;

        public TicketDBRepository(IDictionary<string, string> props)
        {
            this._props = props;
            logger.Info("Creating TicketDBRepository");
        }

        private static Ticket<int> GetTicketFromResultSet(SQLiteDataReader resultSet)
        {
            int id = resultSet.GetInt32(0);
            int idShow = resultSet.GetInt32(1);
            string nameOfCostumer = resultSet.GetString(2);
            return new Ticket<int>(id, idShow, nameOfCostumer);
        }

        public List<Ticket<int>> GetAll()
        {
            logger.Info("finding all tickets");
            var connection = DBUtils.GetConnection(_props);
            var list = new List<Ticket<int>>();
            using (var cmd = new SQLiteCommand("select * from ticket", connection as SQLiteConnection))
            using (var reader = cmd.ExecuteReader())
                while (reader.Read())
                    list.Add(GetTicketFromResultSet(reader));
            logger.Info(list);
            return list;
        }


        public Ticket<int> FindById(int id)
        {
            logger.Info("find a ticket by id");
            var connection = DBUtils.GetConnection(_props);
            var cmd = new SQLiteCommand("select * from ticket where id = " + id,
                connection as SQLiteConnection);
            using (var reader = cmd.ExecuteReader())
            {
                if (reader.Read())
                {
                    Ticket<int> ticket = GetTicketFromResultSet(reader);
                    logger.Info(ticket);
                    return ticket;
                }
            }
            return null;
        }

        public bool Add(Ticket<int> ticket)
        {
            logger.Info("adding ticket");
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand
                ("insert into ticket values (@id, @id_show, @name_of_costumer)",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", null);
                command.Parameters.AddWithValue("@id_show", ticket.IdShow);
                command.Parameters.AddWithValue("@name_of_costumer", ticket.NameOfCostumer);
                command.ExecuteNonQuery();
                logger.InfoFormat("inserted successfully {0}", ticket);
                return true;
            }
        }


        public bool Delete(int id)
        {
            logger.InfoFormat("delete ticket with id = {0}", id);
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand("delete from ticket where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", id);
                command.ExecuteNonQuery();
                logger.InfoFormat("deleted successfully for id {0}", id);
                return true;
            }
        }

        public bool Update(Ticket<int> ticket)
        {
            logger.Info("updating employee");
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand("update ticket set id_show = @id_show, " +
                "name_of_costumer = @name_of_costumer where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id_show", ticket.IdShow);
                command.Parameters.AddWithValue("@name_of_costumer", ticket.NameOfCostumer);
                command.Parameters.AddWithValue("@id", ticket.Id);
                command.ExecuteNonQuery();
                logger.Info("updated successfully");
                return true;
            }
        }
    }
}