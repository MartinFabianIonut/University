using log4net;
using SeventhWeekCSharpServerClient.domain;
using SeventhWeekCSharpServerClient.repository.interfaces;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Data.SQLite;

namespace SeventhWeekCSharpServerClient.repository
{
    public class ShowDBRepository : IShowRepository
    {
        private static readonly ILog Logger = LogManager.GetLogger("ShowDBRepository");

        private readonly IDictionary<string, string> props;

        public ShowDBRepository(IDictionary<string, string> props)
        {
            this.props = props;
            Logger.Info("Creating ShowDBRepository");
        }

        private static Show<int> GetShowFromResultSet(SQLiteDataReader resultSet)
        {
            var id = resultSet.GetInt32(0);
            var title = resultSet.GetString(1);
            var date = resultSet.GetString(2);
            var place = resultSet.GetString(3);
            var idArtist = resultSet.GetInt32(4);
            return new Show<int>(id, title, date, place, idArtist);
        }

        public List<Show<int>> GetAll()
        {
            Logger.Info("finding all shows");
            var connection = DBUtils.GetConnection(props);
            var list = new List<Show<int>>();
            using (var cmd = new SQLiteCommand("select * from show", connection as SQLiteConnection))
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                        list.Add(GetShowFromResultSet(reader));
            Logger.Info(list);
            return list;
        }

        public List<ShowDTO<int>> GetAllShows()
        {
            Logger.Info("finding all shows dto");
            var connection = DBUtils.GetConnection(props);
            var showDTOS = new List<ShowDTO<int>>();
            using (var cmd = new SQLiteCommand("select * from show", connection as SQLiteConnection))
            {
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                    {
                        var available = 0;
                        var unavailable = 0;
                        var sql = "SELECT COUNT(*) as available FROM show INNER JOIN ticket ON show.id = ticket.id_show " +
                                  "WHERE show.id = @show_id and ticket.name_of_costumer is null";
                        var sql2 = "SELECT COUNT(*) as unavailable FROM show INNER JOIN ticket ON show.id = ticket.id_show " +
                                   "WHERE show.id = @show_id and ticket.name_of_costumer is not null";
                        var id = reader.GetInt32(0);
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
            var id = resultSet.GetInt32(0);
            var title = resultSet.GetString(1);
            var date = resultSet.GetString(2);
            var place = resultSet.GetString(3);
            var idArtist = resultSet.GetInt32(4);
            return new ShowDTO<int>(id, title, date, place, available, unavailable, idArtist);
        }

        public Show<int> FindById(int id)
        {
            Logger.Info("find a show by id");
            var connection = DBUtils.GetConnection(props);
            var cmd = new SQLiteCommand("select * from show where id = " + id,
                connection as SQLiteConnection);
            using (var reader = cmd.ExecuteReader())
            {
                if (!reader.Read()) return null;
                var show = GetShowFromResultSet(reader);
                Logger.Info(show);
                return show;
            }
        }

        public bool Add(Show<int> show)
        {
            Logger.Info("adding show");
            var connection = DBUtils.GetConnection(props);
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
                Logger.InfoFormat("inserted successfully {0}", show);
                return true;
            }
        }


        public bool Delete(int id)
        {
            Logger.InfoFormat("delete show with id = {0}", id);
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand("delete from show where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", id);
                command.ExecuteNonQuery();
                Logger.InfoFormat("deleted successfully for id {0}", id);
                return true;
            }
        }

        public bool Update(Show<int> show)
        {
            Logger.Info("updating show");
            var connection = DBUtils.GetConnection(props);
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
                Logger.Info("updated successfully");
                return true;
            }
        }
    }
}
