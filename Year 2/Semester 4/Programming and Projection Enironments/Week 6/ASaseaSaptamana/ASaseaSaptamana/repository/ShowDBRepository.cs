using log4net;
using ASaseaSaptamana.domain;
using ASaseaSaptamana.repository.interfaces;
using ASaseaSaptamana.utils;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Data.SQLite;

namespace ASaseaSaptamana.repository
{
    public class ShowDBRepository : IShowRepository
    {
        private static readonly ILog logger = LogManager.GetLogger("ShowDBRepository");

        private IDictionary<string, string> _props;

        public ShowDBRepository(IDictionary<string, string> props)
        {
            this._props = props;
            logger.Info("Creating ShowDBRepository");
        }

        private static Show<int> GetShowFromResultSet(SQLiteDataReader resultSet)
        {
            int id = resultSet.GetInt32(0);
            string title = resultSet.GetString(1);
            string date = resultSet.GetString(2);
            string place = resultSet.GetString(3);
            int idArtist = resultSet.GetInt32(4);
            return new Show<int>(id, title, date, place, idArtist);
        }

        public List<Show<int>> GetAll()
        {
            logger.Info("finding all shows");
            var connection = DBUtils.GetConnection(_props);
            var list = new List<Show<int>>();
            using (var cmd = new SQLiteCommand("select * from show", connection as SQLiteConnection))
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                        list.Add(GetShowFromResultSet(reader));
            logger.Info(list);
            return list;
        }

        public List<ShowDTO<int>> GetAllShows()
        {
            logger.Info("finding all shows dto");
            var connection = DBUtils.GetConnection(_props);
            var showDTOS = new List<ShowDTO<int>>();
            using (var cmd = new SQLiteCommand("select * from show", connection as SQLiteConnection))
            {
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                    {
                        int available = 0;
                        int unavailable = 0;
                        string sql = "SELECT COUNT(*) as available FROM show INNER JOIN ticket ON show.id = ticket.id_show " +
                                "WHERE show.id = @show_id and ticket.name_of_costumer is null";
                        string sql2 = "SELECT COUNT(*) as unavailable FROM show INNER JOIN ticket ON show.id = ticket.id_show " +
                                "WHERE show.id = @show_id and ticket.name_of_costumer is not null";
                        int id = reader.GetInt32(0);
                        using (var cmd2 = new SQLiteCommand(sql, connection as SQLiteConnection))
                        {
                            cmd2.Parameters.AddWithValue("@show_id", id);
                            using (var reader2 = cmd2.ExecuteReader())
                                if (reader2.Read())
                                    available = reader2.GetInt32(0);
                        }
                        using (var cmd3 = new SQLiteCommand(sql2, connection as SQLiteConnection))
                        {
                            cmd3.Parameters.AddWithValue("@show_id", id);
                            using (var reader3 = cmd3.ExecuteReader())
                                if (reader3.Read())
                                    unavailable = reader3.GetInt32(0);
                        }
                        showDTOS.Add(GetShowDTOFromResultSet(reader, available, unavailable));
                     }
            }
            return showDTOS;
        }

        private ShowDTO<int> GetShowDTOFromResultSet(SQLiteDataReader resultSet, int available, int unavailable)
        {
            int id = resultSet.GetInt32(0);
            string title = resultSet.GetString(1);
            string date = resultSet.GetString(2);
            string place = resultSet.GetString(3);
            int idArtist = resultSet.GetInt32(4);
            return new ShowDTO<int>(id, title, date, place, available, unavailable, idArtist);
        }

        public Show<int> FindById(int id)
        {
            logger.Info("find a show by id");
            var connection = DBUtils.GetConnection(_props);
            var cmd = new SQLiteCommand("select * from show where id = " + id,
                connection as SQLiteConnection);
            using (var reader = cmd.ExecuteReader())
            {
                if (reader.Read())
                {
                    Show<int> show = GetShowFromResultSet(reader);
                    logger.Info(show);
                    return show;
                }
            }
            return null;
        }

        public bool Add(Show<int> show)
        {
            logger.Info("adding show");
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand
                ("insert into show values (@id, @title, @date, @place, @id_artist)",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", null);
                command.Parameters.AddWithValue("@title", show.Title);
                command.Parameters.AddWithValue("@date", show.Date);
                command.Parameters.AddWithValue("@place", show.Place);
                command.Parameters.AddWithValue("@id_artist", show.IdArtist);
                command.ExecuteNonQuery();
                logger.InfoFormat("inserted successfully {0}", show);
                return true;
            }
        }


        public bool Delete(int id)
        {
            logger.InfoFormat("delete show with id = {0}", id);
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand("delete from show where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", id);
                command.ExecuteNonQuery();
                logger.InfoFormat("deleted successfully for id {0}", id);
                return true;
            }
        }

        public bool Update(Show<int> show)
        {
            logger.Info("updating show");
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand("update show set title = @title, " +
                "date = @date, place = @place, id_artist = @id_artist where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@title", show.Title);
                command.Parameters.AddWithValue("@date", show.Date);
                command.Parameters.AddWithValue("@place", show.Place);
                command.Parameters.AddWithValue("@id_artist", show.IdArtist);
                command.Parameters.AddWithValue("@id", show.Id);
                command.ExecuteNonQuery();
                logger.Info("updated successfully");
                return true;
            }
        }
    }
}
