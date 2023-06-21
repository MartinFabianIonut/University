package app.rest.persistance.repository.jdbc;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IPositionRepository;
import app.rest.persistance.repository.IScoreRepository;
import app.rest.persistance.repository.IPlayerRepository;
import domain.Game;
import domain.Position;
import domain.Score;
import domain.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component()
public class ScoreDBIRepository implements IScoreRepository<Integer, Score> {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private final IPlayerRepository<Integer, Player> playerDBIRepository;
    private final IGameRepository<Integer, Game> gameORMDBIRepository;
    private final IPositionRepository<Integer, Position> positionORMDBIRepository;

    public ScoreDBIRepository(Properties props, IPlayerRepository<Integer, Player> playerDBIRepository,
                              IGameRepository<Integer, Game> gameORMDBIRepository,
                              IPositionRepository<Integer, Position> positionORMDBIRepository) {
        logger.info("Initializing ScoreDBIRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        this.playerDBIRepository = playerDBIRepository;
        this.gameORMDBIRepository = gameORMDBIRepository;
        this.positionORMDBIRepository = positionORMDBIRepository;
    }

    private Score getScoreFromResultSet(ResultSet resultSet) throws SQLException {
        int score_id = resultSet.getInt("score_id");
        int game_id = resultSet.getInt("game_id");
        Game game = gameORMDBIRepository.findById(game_id);
        int player_id = resultSet.getInt("player_id");
        Player player = playerDBIRepository.findById(player_id);
        int position_id = resultSet.getInt("position_id");
        Position guess = positionORMDBIRepository.findById(position_id);
        Integer points = resultSet.getInt("points");
        Integer totalPoints = resultSet.getInt("totalPoints");
        Integer round = resultSet.getInt("round");
        Integer gameOver = resultSet.getInt("gameOver");
        Time timeValue = resultSet.getTime("time");
        if (timeValue != null) {
            // Convert the time to LocalTime
            LocalTime time = timeValue.toLocalTime();
            return new Score(score_id, game, player, guess, points, totalPoints, round, gameOver, time);
        } else return new Score(score_id, game, player, guess, points, totalPoints, round,gameOver, null);
    }

    @Override
    public Score findById(Integer id) {
        logger.traceEntry("find a score by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from score where score_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Score score = getScoreFromResultSet(resultSet);
                    logger.traceExit(score);
                    return score;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in ScoreDBIRepository: " + exception);
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
            logger.error("ERROR for getAll in ScoreDBIRepository: " + exception);
        }
        logger.traceExit(scores);
        return scores;
    }

    @Override
    public boolean add(Score score) {
        logger.traceEntry("Add score {}", score);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("insert into score " +
                "(game_id, player_id, position_id, points, totalPoints, round, gameOver, time) " +
                "values (?, ?, ?, ?, ?, ?,?, ?)")) {
            setParameters(score, ps);
            ps.setInt(7, score.getGameOver());
            ps.setTime(8, Time.valueOf(score.getTime()));
            int result = ps.executeUpdate();
            logger.trace("saved {} instance", result);
        } catch (SQLException exception) {
            logger.error("ERROR for insert in ScoreDBIRepository: " + exception);
            return false;
        }
        logger.traceExit("inserted successfully");
        return true;
    }

    private void setParameters(Score score, PreparedStatement ps) throws SQLException {
        ps.setInt(1, score.getGame().getId());
        ps.setInt(2, score.getPlayer().getId());
        ps.setInt(3, score.getGuess().getId());
        ps.setInt(4, score.getPoints());
        ps.setInt(5, score.getTotalPoints());
        ps.setInt(6, score.getRound());
    }

    @Override
    public boolean update(Score score) {
        logger.traceEntry("updating score");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("update score set " +
                "game_id = ?, player_id = ?, position_id =?, " +
                "points = ?, totalPoints=?, round=?, gameOver = ?, time=? where score_id = ?")) {
            setParameters(score, ps);
            ps.setTime(7, Time.valueOf(score.getTime()));
            ps.setInt(8, score.getGameOver());
            ps.setInt(9, score.getId());
            int result = ps.executeUpdate();
            logger.trace("updated {} instance", result);
            if (result == 0) {
                return false;
            }
        } catch (SQLException exception) {
            logger.error("ERROR for update in ScoreDBIRepository: " + exception);
            return false;
        }
        logger.traceExit("updated successfully");
        return true;
    }

    @Override
    public Score getLatestScoreForPlayer(Integer idPlayer) {
        logger.traceEntry("find a score by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps =
                     connection.prepareStatement("select * from score where player_id = ? " +
                             "order by score_id desc limit 1")) {
            ps.setInt(1, idPlayer);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Score score = getScoreFromResultSet(resultSet);
                    logger.traceExit(score);
                    return score;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in ScoreDBIRepository: " + exception);
        }
        return null;
    }

    @Override
    public Integer getMaxId() {
        logger.traceEntry("find a score by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select max(score_id) from score")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt(1);
                    logger.traceExit(id);
                    return id;
                }
            }
        } catch (SQLException exception) {
            logger.error("ERROR for findById in ScoreDBIRepository: " + exception);
        }
        return null;
    }
}
