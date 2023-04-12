package repository;

import domain.Show;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShowDBRepository extends AbstractRepository<Integer, Show> {

    public ShowDBRepository(Properties props) {
        super(props);
    }

    private Show getShowFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        String place = resultSet.getString("place");
        int idArtist = resultSet.getInt("id_artist");
        return new Show(id, title, date, place, idArtist);
    }

    @Override
    public Show findById(Integer id) {
        logger.traceEntry("find a show by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from show where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Show show = getShowFromResultSet(resultSet);
                    logger.traceExit(show);
                    return show;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in ShowDBRepository: " + exception);
        }
        return null;
    }

    @Override
    public Iterable<Show> getAll() {
        logger.traceEntry("finding all tickets");
        Connection connection = dbUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from show")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    shows.add(getShowFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for getAll in ShowDBRepository: " + exception);
        }
        logger.traceExit(shows);
        return shows;
    }

    @Override
    public boolean add(Show show) {
        logger.traceEntry("adding show {}", show);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "insert into show (title,date,place,id_artist) values (?, ?, ?, ?)")) {
            ps.setString(1, show.getTitle());
            ps.setString(2, show.getDate().toString());
            ps.setString(3, show.getPlace());
            ps.setInt(4, show.getIdArtist());
            int result = ps.executeUpdate();
            logger.trace("saved {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for insert in ShowDBRepository: " + exception);
            return false;
        }
        logger.traceExit("inserted successfully");
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        logger.traceEntry("delete ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("delete from show where id=?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("deleted {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for delete in ShowDBRepository: " + exception);
            return false;
        }
        logger.traceExit("deleted successfully");
        return true;
    }

    @Override
    public boolean update(Show show) {
        logger.traceEntry("updating ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "update show set title = ?, date = ?, place = ?, id_artist = ?" +
                " where id = ?")) {
            ps.setString(1, show.getTitle());
            ps.setString(2, show.getDate().toString());
            ps.setString(3, show.getPlace());
            ps.setInt(4, show.getIdArtist());
            ps.setInt(5, show.getId());
            int result = ps.executeUpdate();
            logger.trace("updated {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for update in ShowDBRepository: " + exception);
            return false;
        }
        logger.traceExit("updated successfully");
        return true;
    }
}
