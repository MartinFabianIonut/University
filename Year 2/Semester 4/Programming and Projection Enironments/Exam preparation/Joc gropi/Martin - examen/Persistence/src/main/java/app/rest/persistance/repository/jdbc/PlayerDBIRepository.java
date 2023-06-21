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
        logger.info("Initializing playerDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    private Player getPlayerFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("player_id");
        String alias = resultSet.getString("alias");
        return new Player(id, alias);
    }

    @Override
    public Player findById(Integer id) {
        logger.traceEntry("find a player by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from player where player_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Player player = getPlayerFromResultSet(resultSet);
                    logger.traceExit(player);
                    return player;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in PlayerDBRepository: " + exception);
        }
        return null;
    }


    @Override
    public Iterable<Player> getAll() {
        logger.traceEntry("finding all players");
        Connection connection = dbUtils.getConnection();
        List<Player> players = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from player")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    players.add(getPlayerFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for getAll in PlayerDBRepository: " + exception);
        }
        logger.traceExit(players);
        return players;
    }

    @Override
    public boolean add(Player player) {
        logger.traceEntry("adding player {}", player);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("insert into player (alias) values (?)")) {
            ps.setString(1, player.getAlias());
            int result = ps.executeUpdate();
            logger.trace("saved {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for insert in playerDBRepository: " + exception);
            return false;
        }
        logger.traceExit("inserted successfully");
        return true;
    }

    @Override
    public boolean update(Player player) {
        logger.traceEntry("updating player");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("update player set alias = ?" +
                " where player_id = ?")) {
            ps.setString(1, player.getAlias());
            ps.setInt(2, player.getId());
            int result = ps.executeUpdate();
            logger.trace("updated {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for update in PlayerDBRepository: " + exception);
            return false;
        }
        logger.traceExit("updated successfully");
        return true;
    }

    public Player login(String username) {
        logger.traceEntry("find a player by username and password");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from player where alias = ?")) {
            ps.setString(1,username);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Player player = getPlayerFromResultSet(resultSet);
                    logger.traceExit(player);
                    return player;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for authenticate player in PlayerDBRepository: " + exception);
        }
        logger.traceExit("authentication failed");
        return null;
    }
}
