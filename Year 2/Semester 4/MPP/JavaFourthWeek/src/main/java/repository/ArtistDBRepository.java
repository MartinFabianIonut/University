package repository;

import domain.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ArtistDBRepository extends AbstractRepository<Integer, Artist> {

    public ArtistDBRepository(Properties props) {
        super(props);
    }

    private Artist getArtistFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        return new Artist(id, firstName, lastName);
    }

    @Override
    public Artist findById(Integer id) {
        logger.traceEntry("find an artist by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from artist where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Artist artist = getArtistFromResultSet(resultSet);
                    logger.traceExit(artist);
                    return artist;
                }
            }
        } catch (SQLException exception) {
            logger.error(exception);
            System.err.println("ERROR for findById in ArtistDBRepository: " + exception);
        }
        return null;
    }

    @Override
    public Iterable<Artist> getAll() {
        logger.traceEntry("finding all artists");
        Connection connection = dbUtils.getConnection();
        List<Artist> artists = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from artist")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    artists.add(getArtistFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error(exception);
            System.err.println("ERROR for getAll in ArtistDBRepository: " + exception);
        }
        logger.traceExit(artists);
        return artists;
    }

    @Override
    public boolean add(Artist artist) {
        logger.traceEntry("adding artist {}", artist);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "insert into artist (first_name, last_name) values (?, ?)")) {
            ps.setString(1, artist.getFirstName());
            ps.setString(2, artist.getLastName());
            int result = ps.executeUpdate();
            logger.trace("saved {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for insert in ArtistDBRepository: " + exception);
            return false;
        }
        logger.traceExit("inserted successfully");
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        logger.traceEntry("delete artist");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("delete from artist where id=?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("deleted {} instance", result);
            if (result == 0)
                return false;
        } catch (SQLException exception) {
            logger.error("ERROR for delete in ArtistDBRepository: " + exception);
            return false;
        }
        logger.traceExit("deleted successfully");
        return true;
    }

    @Override
    public boolean update(Artist artist) {
        logger.traceEntry("updating artist");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "update artist set first_name = ?, last_name = ?" +
                " where id = ?")) {
            ps.setString(1, artist.getFirstName());
            ps.setString(2, artist.getLastName());
            ps.setInt(3, artist.getId());
            int result = ps.executeUpdate();
            logger.trace("updated {} instance", result);
            if (result == 0)
                return false;
        } catch (SQLException exception) {
            logger.error("ERROR for update in ArtistDBRepository: " + exception);
            return false;
        }
        logger.traceExit("updated successfully");
        return true;
    }
}
