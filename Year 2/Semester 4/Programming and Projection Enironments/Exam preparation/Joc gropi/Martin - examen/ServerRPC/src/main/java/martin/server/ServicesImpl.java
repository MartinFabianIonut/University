package martin.server;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IPlayerRepository;
import app.rest.persistance.repository.IPositionRepository;
import app.rest.persistance.repository.IScoreRepository;
import domain.*;
import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ServicesImpl implements IService {
    private final IPlayerRepository<Integer, Player> playerRepository;
    private final IGameRepository<Integer, Game> gameRepository;
    private final IScoreRepository<Integer, Score> scoreRepository;
    private final IPositionRepository<Integer, Position> positionRepository;
    private final Map<Integer, IObserver> loggedOnes;
    private final Map<Player, Game> gamePlayerMap = new HashMap<>();

    public ServicesImpl(IPlayerRepository<Integer, Player> playerRepository,
                        IGameRepository<Integer, Game> gameRepository,
                        IScoreRepository<Integer, Score> scoreRepository,
                        IPositionRepository<Integer, Position> positionRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.scoreRepository = scoreRepository;
        this.positionRepository = positionRepository;
        loggedOnes = new ConcurrentHashMap<>();
    }

    @Override
    public Iterable<DTO> getRanking() {
        List<DTO> list = new ArrayList<>();
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        scores = scores.stream()
                .filter(score -> score.getGameOver() == 1)
                .collect(java.util.stream.Collectors.toList());
        List<Score> allScores = (List<Score>) scoreRepository.getAll();
        for (Score score : scores) {
            int seconds = 0;
            for (Score score1 : allScores) {
                if (score1.getPlayer().getId().equals(score.getPlayer().getId()) && score1.getRound()==0
                        && score1.getGame().getId().equals(score.getGame().getId())) {
                    Duration duration = Duration.between(score1.getTime(), score.getTime());
                    long milliseconds = duration.toMillis();
                    seconds += milliseconds;
                }
            }
            DTO dto = new DTO(score.getPlayer(), seconds, score.getTotalPoints());
            list.add(dto);
        }
        // sort descending by points and ascending by seconds
        list.sort((o1, o2) -> {
            if (o1.getPoints().equals(o2.getPoints()))
                return o1.getSeconds().compareTo(o2.getSeconds());
            return o2.getPoints().compareTo(o1.getPoints());
        });
        return list;
    }

    private void update(Player player) throws MyException {
        for (IObserver observer : loggedOnes.values()) {
            observer.updateRanking();
        }
        for (Integer id : loggedOnes.keySet()) {
            if (id.equals(player.getId())) {
                loggedOnes.get(id).updateControl(player);
            }
        }
    }

    @Override
    public String guess(Pair player_coordinates) throws MyException {
        Player player = player_coordinates.getPlayer();
        Game game = gamePlayerMap.get(player);
        if (game == null)
            throw new MyException("Player not in game.");
        Score oldScore = scoreRepository.getLatestScoreForPlayer(player.getId());
        int row = player_coordinates.getRow();
        int col = player_coordinates.getCol();
        if (row > oldScore.getRound())
            throw new MyException("You can't guess in the future.");
        if (row < oldScore.getRound())
            throw new MyException("You already guessed for this round.");
        // verify if the coordinates are in the game
        if ((game.getPosition1().getRow() == row && game.getPosition1().getCol() == col) ||
                (game.getPosition2().getRow() == row && game.getPosition2().getCol() == col) ||
                (game.getPosition3().getRow() == row && game.getPosition3().getCol() == col) ||
                (game.getPosition4().getRow() == row && game.getPosition4().getCol() == col) ||
                (game.getPosition5().getRow() == row && game.getPosition5().getCol() == col)) {
            Position position = positionRepository.findByRowCol(row, col);
            Score newScore = new Score(0, game, player, position, 0, oldScore.getTotalPoints(), oldScore.getRound()+1, 1, LocalTime.now());
            scoreRepository.add(newScore);
            update(player);
            return "You lost!";
        }
        Position newPosition = new Position(0, row, col);
        Position position = positionRepository.findByRowCol(row, col);
        if (position == null) {
            positionRepository.add(newPosition);
            position = positionRepository.findByRowCol(row, col);
        }
        int points = row + 1;
        int oldPoints = oldScore.getTotalPoints();
        int newPoints = oldPoints + points;
        int oldRound = oldScore.getRound();
        int newRound = oldRound + 1;
        if (newRound == 4) {
            Score newScore = new Score(0, game, player, position, points, newPoints, newRound, 1, LocalTime.now());
            scoreRepository.add(newScore);
            update(player);
            return "You win!";
        }
        Score newScore = new Score(0, game, player, position, points, newPoints, newRound, 0, LocalTime.now());
        scoreRepository.add(newScore);
        return "Ok!";
    }

    @Override
    public String setControlGameOver(Player player) {
        Game game = gamePlayerMap.get(player);
        gamePlayerMap.remove(player);
        Score oldScore = scoreRepository.getLatestScoreForPlayer(player.getId());
        String control = "Points: " + oldScore.getTotalPoints() + "\n" ;
        control += "Positions: " + game;
        List<DTO> list = (List<DTO>) getRanking();
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPlayer().getId().equals(player.getId()) &&
                    Objects.equals(list.get(i).getPoints(), oldScore.getTotalPoints())){
                index = i + 1;
                break;
            }
        }
        control += "\nRanking: " + index + " out of " + list.size() + " games finished.";
        return control;
    }


    @Override
    public synchronized Player login(Player player, IObserver iObserver) throws MyException {
        Player toLogin = playerRepository.login(player.getAlias());
        if (toLogin != null) {
            if (loggedOnes.get(toLogin.getId()) != null)
                throw new MyException("User already logged in.");
            loggedOnes.put(toLogin.getId(), iObserver);
            // choose random 5 positions from positionRepository
            // at least 1 have row = 0, 1 have row = 1, 1 have row = 2, 1 have row = 3
            List<Position> positions = (List<Position>) positionRepository.getAll();
            List<Position> randomPositions = new ArrayList<>();
            int row0 = 0, row1 = 0, row2 = 0, row3 = 0;
            while (randomPositions.size() < 5) {
                int randomPosition = (int) (Math.random() * positions.size());
                Position position = positions.get(randomPosition);
                if (position.getRow() == 0 && row0 == 0) {
                    randomPositions.add(position);
                    row0++;
                } else {
                    if (position.getRow() == 1 && row1 == 0 && !randomPositions.contains(position)) {
                        randomPositions.add(position);
                        row1++;
                    } else {
                        if (position.getRow() == 2 && row2 == 0 && !randomPositions.contains(position)) {
                            randomPositions.add(position);
                            row2++;
                        } else {
                            if (position.getRow() == 3 && row3 == 0 && !randomPositions.contains(position)) {
                                randomPositions.add(position);
                                row3++;
                            } else {
                                if (!randomPositions.contains(position))
                                    randomPositions.add(position);
                            }
                        }
                    }
                }
            }
            Game game = new Game(0, randomPositions.get(0), randomPositions.get(1), randomPositions.get(2), randomPositions.get(3), randomPositions.get(4));
            gameRepository.add(game);
            gamePlayerMap.put(toLogin, game);
            Score score = new Score(0, game, toLogin, new Position(0,-1,-1), 0, 0, 0, 0, LocalTime.now());
            scoreRepository.add(score);
        } else
            throw new MyException("Authentication failed.");
        return toLogin;
    }

    @Override
    public synchronized void logout(Player game, IObserver iObserver) throws MyException {
        IObserver localClient = loggedOnes.remove(game.getId());
        if (localClient == null)
            throw new MyException("User " + game.getId() + " is not logged in.");
    }

}
