package app.rest.persistance.repository.jdbc;

import app.rest.persistance.repository.IPlayerRepository;
import domain.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component()
public class PlayerDBIRepository implements IPlayerRepository<Integer, Player> {

    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public PlayerDBIRepository(Properties props) {
        logger.info("Initializing EmployeeDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    private Player getPlayerFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("player_id");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        return new Player(id, firstName, lastName, username, password);
    }

    @Override
    public Player findById(Integer id) {
        logger.traceEntry("find a employee by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from player where player_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Player game = getPlayerFromResultSet(resultSet);
                    logger.traceExit(game);
                    return game;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in EmployeeDBRepository: " + exception);
        }
        return null;
    }


    @Override
    public Iterable<Player> getAll() {
        logger.traceEntry("finding all tickets");
        Connection connection = dbUtils.getConnection();
        List<Player> players = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from player")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    players.add(getPlayerFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for getAll in EmployeeDBRepository: " + exception);
        }
        logger.traceExit(players);
        return players;
    }

    @Override
    public boolean add(Player game) {
        logger.traceEntry("adding game {}", game);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "insert into player (firstName, lastName, username, password) values (?, ?, ?, ?)")) {
            ps.setString(1, game.getFirstName());
            ps.setString(2, game.getLastName());
            ps.setString(3, game.getUsername());
            ps.setString(4, game.getPassword());
            int result = ps.executeUpdate();
            logger.trace("saved {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for insert in EmployeeDBRepository: " + exception);
            return false;
        }
        logger.traceExit("inserted successfully");
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        logger.traceEntry("delete ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("delete from player where player_id=?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("deleted {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for delete in EmployeeDBRepository: " + exception);
            return false;
        }
        logger.traceExit("deleted successfully");
        return true;
    }

    @Override
    public boolean update(Player game) {
        logger.traceEntry("updating ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "update player set firstName = ?, lastName = ?, username = ?, password = ?" +
                " where player_id = ?")) {
            ps.setString(1, game.getFirstName());
            ps.setString(2, game.getLastName());
            ps.setString(3, game.getUsername());
            ps.setString(4, game.getPassword());
            ps.setInt(5, game.getId());
            int result = ps.executeUpdate();
            logger.trace("updated {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for update in EmployeeDBRepository: " + exception);
            return false;
        }
        logger.traceExit("updated successfully");
        return true;
    }

    public Player login(String username, String password) {
        logger.traceEntry("find a employee by username and password");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from player where username = ? and password = ?")) {
            ps.setString(1,username);
            ps.setString(2,password);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Player player = getPlayerFromResultSet(resultSet);
                    logger.traceExit(player);
                    return player;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for authenticateEmployee in EmployeeDBRepository: " + exception);
        }
        logger.traceExit("authentication failed");
        return null;
    }
}
