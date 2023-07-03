using log4net;
using ProtobuffProject.domain;
using ProtobuffProject.repository.interfaces;
using System.Collections.Generic;
using System.Data.SQLite;

namespace ProtobuffProject.repository
{
    public class ArtistDBRepository: IArtistRepository
    {
        private static readonly ILog Logger = LogManager.GetLogger("ArtistDBRepository");

        private readonly IDictionary<string, string> props;

        public ArtistDBRepository(IDictionary<string, string> props)
        {
            this.props = props;
            Logger.Info("Creating ArtistDBRepository");
        }

        private static Artist<int> GetArtistFromResultSet(SQLiteDataReader resultSet)
        {
            var id = resultSet.GetInt32(0);
            var firstName = resultSet.GetString(1);
            var lastName = resultSet.GetString(2);
            return new Artist<int>(id, firstName, lastName);
        }
        
        public List<Artist<int>> GetAll() {
            Logger.Info("finding all artists");
            var connection = DBUtils.GetConnection(props);
            var list = new List<Artist<int>>();
            using (var cmd = new SQLiteCommand("select * from artist", connection as SQLiteConnection))     
                using (var reader = cmd.ExecuteReader())
                    while (reader.Read())
                        list.Add(GetArtistFromResultSet(reader));
            Logger.Info(list);
            return list;
        }
            

        public Artist<int> FindById(int id)
        {
            Logger.Info("find an artist by id");
            var connection = DBUtils.GetConnection(props);
            var cmd = new SQLiteCommand("select * from artist where id = " + id,
                connection as SQLiteConnection);
            using (var reader = cmd.ExecuteReader())
            {
                if (!reader.Read()) return null;
                var artist = GetArtistFromResultSet(reader);
                Logger.Info(artist);
                return artist;
            }
        }

        public bool Add(Artist<int> artist)
        {
            Logger.Info("adding artist");
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand
                ("insert into artist values (@id, @first_name, @last_name)",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", null);
                command.Parameters.AddWithValue("@first_name", artist.FirstName);
                command.Parameters.AddWithValue("@last_name", artist.LastName);
                command.ExecuteNonQuery();
                Logger.InfoFormat("inserted successfully {0}", artist);
                return true;
            }
        }


        public bool Delete(int id)
        {
            Logger.InfoFormat("delete artist with id = {0}", id);
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand("delete from artist where id = @id",
                      connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@id", id);
                command.ExecuteNonQuery();
                Logger.InfoFormat("deleted successfully for id {0}", id);
                return true;
            }
        }

        public bool Update(Artist<int> artist)
        {
            Logger.Info("updating artist");
            var connection = DBUtils.GetConnection(props);
            using (var command = new SQLiteCommand("update artist set first_name = @first_name, " +
                "last_name = @last_name where id = @id", connection as SQLiteConnection))
            {
                command.Parameters.AddWithValue("@first_name", artist.FirstName);
                command.Parameters.AddWithValue("@last_name", artist.LastName);
                command.Parameters.AddWithValue("@id", artist.Id);
                command.ExecuteNonQuery();
                Logger.Info("updated successfully");
                return true;
            }
        }
    }
}
