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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component()
public class ScoreDBIRepository implements IScoreRepository<Integer, Score> {

    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private final IPlayerRepository<Integer,Player> playerDBIRepository;
    private final IGameRepository<Integer,Game> gameORMDBIRepository;

    public ScoreDBIRepository(Properties props, IPlayerRepository<Integer,Player> playerDBIRepository, IGameRepository<Integer,Game> gameORMDBIRepository) {
        logger.info("Initializing MoveDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.playerDBIRepository = playerDBIRepository;
        this.gameORMDBIRepository = gameORMDBIRepository;
    }

    private Score getMoveFromResultSet(ResultSet resultSet) throws SQLException {
        int score_id = resultSet.getInt("score_id");
        int game_id = resultSet.getInt("game_id");
        Game game = gameORMDBIRepository.findById(game_id);
        int player_who_proposed_id = resultSet.getInt("player_who_proposed_id");
        Player player_who_proposed = playerDBIRepository.findById(player_who_proposed_id);
        int player_who_guessed_id = resultSet.getInt("player_who_guessed_id");
        Player player_who_guessed = playerDBIRepository.findById(player_who_guessed_id);
        Integer round= resultSet.getInt("round");
        Integer points = resultSet.getInt("points");
        Integer guess = resultSet.getInt("guess");
        return new Score(score_id, game, player_who_proposed, player_who_guessed, round, points, guess);
    }

    @Override
    public Score findById(Integer id) {
        logger.traceEntry("find a move by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from score where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Score score = getMoveFromResultSet(resultSet);
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
                    scores.add(getMoveFromResultSet(resultSet));
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
                "insert into score (game_id, player_who_proposed_id, player_who_guessed_id, round, points, guess) " +
                "values (?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, score.getGame().getId());
            ps.setInt(2, score.getPlayer_who_proposed().getId());
            if (score.getPlayer_who_guessed() == null)
                ps.setNull(3, java.sql.Types.INTEGER);
            else
                ps.setInt(3, score.getPlayer_who_guessed().getId());
            ps.setInt(4, score.getRound());
            ps.setInt(5, score.getPoints());
            ps.setInt(6, score.getGuess());
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
        try (PreparedStatement ps = connection.prepareStatement("delete from score where id=?")) {
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
                "update score set game_id = ?, player_who_proposed_id = ?, player_who_guessed_id = ?, " +
                "round =?, points=?, guess=? where id = ?")) {
            ps.setInt(1, score.getGame().getId());
            ps.setInt(2, score.getPlayer_who_proposed().getId());
            ps.setInt(3, score.getPlayer_who_guessed().getId());
            ps.setInt(4, score.getRound());
            ps.setInt(5, score.getPoints());
            ps.setInt(6, score.getGuess());
            ps.setInt(7, score.getId());
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
    public Score getLatestMoveForPlayer(Integer idPlayer) {
        logger.traceEntry("find a move by id");
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("" +
                "select * from score where player_who_guessed_id = ? order by score_id desc limit 1")) {
            ps.setInt(1, idPlayer);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Score score = getMoveFromResultSet(resultSet);
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
