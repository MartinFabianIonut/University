using log4net;
using SaptamanaAPatra.domain;
using SaptamanaAPatra.repository.interfaces;
using SaptamanaAPatra.utils;
using System.Collections.Generic;
using System.Data.SQLite;

namespace SaptamanaAPatra.repository
{
    internal class ShowDBRepository : IShowRepository
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
