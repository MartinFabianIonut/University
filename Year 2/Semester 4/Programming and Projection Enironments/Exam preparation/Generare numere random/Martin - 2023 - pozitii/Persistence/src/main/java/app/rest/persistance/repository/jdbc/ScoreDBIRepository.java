package app.rest.persistance.repository.jdbc;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IScoreRepository;
import app.rest.persistance.repository.IPlayerRepository;
import domain.Game;
import domain.Score;
import domain.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component()
public class ScoreDBIRepository implements IScoreRepository<Integer, Score> {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private final IPlayerRepository<Integer, Player> playerDBIRepository;
    private final IGameRepository<Integer, Game> gameORMDBIRepository;

    public ScoreDBIRepository(Properties props, IPlayerRepository<Integer, Player> playerDBIRepository, IGameRepository<Integer, Game> gameORMDBIRepository) {
        logger.info("Initializing MoveDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        this.playerDBIRepository = playerDBIRepository;
        this.gameORMDBIRepository = gameORMDBIRepository;
    }

    private Score getScoreFromResultSet(ResultSet resultSet) throws SQLException {
        int score_id = resultSet.getInt("score_id");
        int game_id = resultSet.getInt("game_id");
        Game game = gameORMDBIRepository.findById(game_id);
        int player_id = resultSet.getInt("player_id");
        Player player = playerDBIRepository.findById(player_id);
        Integer position = resultSet.getInt("position");
        Integer sum = resultSet.getInt("sum");
        Integer totalSum = resultSet.getInt("totalSum");
        Integer gameOver = resultSet.getInt("gameOver");
        String datetimeString = resultSet.getString("time");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(datetimeString, formatter);
        return new Score(score_id, game, player, position, sum, totalSum, gameOver, localDateTime);
    }

    @Override
    public Score findById(Integer id) {
        logger.traceEntry("find a move by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from score where score_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Score score = getScoreFromResultSet(resultSet);
                    logger.traceExit(score);
                    return score;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in MoveDBRepository: " + exception);
        }
        return null;
    }

    @Override
    public Iterable<Score> getAll() {
        logger.traceEntry("finding all scores");
        Connection connection = dbUtils.getConnection();
        List<Score> scores = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from score")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    scores.add(getScoreFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for getAll in MoveDBRepository: " + exception);
        }
        logger.traceExit(scores);
        return scores;
    }

    @Override
    public boolean add(Score score) {
        logger.traceEntry("Add score {}", score);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "insert into score (game_id, player_id, position, sum, totalSum, gameOver, time) " +
                "values (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, score.getGame().getId());
            ps.setInt(2, score.getPlayer().getId());
            ps.setInt(3, score.getPosition());
            ps.setInt(4, score.getSum());
            ps.setInt(5, score.getTotalSum());
            ps.setInt(6, score.getGameOver());
            LocalDateTime localDateTime = score.getTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String datetimeString = localDateTime.format(formatter);
            ps.setString(7, datetimeString);
            int result = ps.executeUpdate();
            logger.trace("saved {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for insert in MoveDBRepository: " + exception);
            return false;
        }
        logger.traceExit("inserted successfully");
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        logger.traceEntry("delete ticket");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("delete from score where score_id=?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            logger.trace("deleted {} instance", result);

        } catch (SQLException exception) {
            logger.error("ERROR for delete in TicketDBRepository: " + exception);
            return false;
        }
        logger.traceExit("deleted successfully");
        return true;
    }

    @Override
    public boolean update(Score score) {
        logger.traceEntry("updating score");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "update score set game_id = ?, player_id = ?, position = ?, sum = ?," +
                "totalSum = ?, gameOver = ?, time=? where score_id = ?")) {
            ps.setInt(1, score.getGame().getId());
            ps.setInt(2, score.getPlayer().getId());
            ps.setInt(3, score.getPosition());
            ps.setInt(4, score.getSum());
            ps.setInt(5, score.getTotalSum());
            ps.setInt(6, score.getGameOver());
            LocalDateTime localDateTime = score.getTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String datetimeString = localDateTime.format(formatter);
            ps.setString(7, datetimeString);
            ps.setInt(8, score.getId());
            int result = ps.executeUpdate();
            logger.trace("updated {} instance", result);
            if (result == 0) {
                return false;
            }
        } catch (SQLException exception) {
            logger.error("ERROR for update in MoveDBRepository: " + exception);
            return false;
        }
        logger.traceExit("updated successfully");
        return true;
    }

    @Override
    public Score getLatestScoreForPlayer(Integer idPlayer) {
        logger.traceEntry("find a move by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from score where player_id = ? order by score_id desc limit 1")) {
            ps.setInt(1, idPlayer);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Score score = getScoreFromResultSet(resultSet);
                    logger.traceExit(score);
                    return score;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in MoveDBRepository: " + exception);
        }
        return null;
    }

    @Override
    public Integer getMaxId() {
        logger.traceEntry("find a move by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select max(score_id) from score")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt(1);
                    logger.traceExit(id);
                    return id;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in MoveDBRepository: " + exception);
        }
        return null;
    }
}
