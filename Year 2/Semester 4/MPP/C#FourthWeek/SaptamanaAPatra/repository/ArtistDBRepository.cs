using log4net;
using SaptamanaAPatra.domain;
using SaptamanaAPatra.repository.interfaces;
using SaptamanaAPatra.utils;
using System.Collections.Generic;
using System.Data.SQLite;

namespace SaptamanaAPatra.repository
{
    internal class ArtistDBRepository: IArtistRepository
    {
        private static readonly ILog logger = LogManager.GetLogger("ArtistDBRepository");

        private IDictionary<string, string> _props;

        public ArtistDBRepository(IDictionary<string, string> props)
        {
            this._props = props;
            logger.Info("Creating ArtistDBRepository");
        }

        private static Artist<int> GetArtistFromResultSet(SQLiteDataReader resultSet)
        {
            int id = resultSet.GetInt32(0);
            string firstName = resultSet.GetString(1);
            string lastName = resultSet.GetString(2);
            return new Artist<int>(id, firstName, lastName);
        }
        
        public List<Artist<int>> GetAll() {
            logger.Info("finding all artists");
            var connection = DBUtils.GetConnection(_props);
            var list = new List<Artist<int>>();
            using (var cmd = new SQLiteCommand("select * from artist", connection as SQLiteConnection))     
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                        list.Add(GetArtistFromResultSet(reader));
            logger.Info(list);
            return list;
        }
            

        public Artist<int> FindById(int id)
        {
            logger.Info("find an artist by id");
            var connection = DBUtils.GetConnection(_props);
            var cmd = new SQLiteCommand("select * from artist where id = " + id,
                connection as SQLiteConnection);
            using (var reader = cmd.ExecuteReader())
            {
                if (reader.Read())
                {
                    Artist<int> artist = GetArtistFromResultSet(reader);
                    logger.Info(artist);
                    return artist;
                }
            }
            return null;
        }

        public bool Add(Artist<int> artist)
        {
            logger.Info("adding artist");
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand
                ("insert into artist values (@id, @first_name, @last_name)",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", null);
                command.Parameters.AddWithValue("@first_name", artist.FirstName);
                command.Parameters.AddWithValue("@last_name", artist.LastName);
                command.ExecuteNonQuery();
                logger.InfoFormat("inserted successfully {0}", artist);
                return true;
            }
        }


        public bool Delete(int id)
        {
            logger.InfoFormat("delete artist with id = {0}", id);
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand("delete from artist where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", id);
                command.ExecuteNonQuery();
                logger.InfoFormat("deleted successfully for id {0}", id);
                return true;
            }
        }

        public bool Update(Artist<int> artist)
        {
            logger.Info("updating artist");
            var connection = DBUtils.GetConnection(_props);
            using (var command = new SQLiteCommand("update artist set first_name = @first_name, " +
                "last_name = @last_name where id = @id", connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@first_name", artist.FirstName);
                command.Parameters.AddWithValue("@last_name", artist.LastName);
                command.Parameters.AddWithValue("@id", artist.Id);
                command.ExecuteNonQuery();
                logger.Info("updated successfully");
                return true;
            }
        }
    }
}
