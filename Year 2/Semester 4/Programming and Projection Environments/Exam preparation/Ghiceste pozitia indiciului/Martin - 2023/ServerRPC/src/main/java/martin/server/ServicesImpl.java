package martin.server;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IPlayerRepository;
import app.rest.persistance.repository.IScoreRepository;
import domain.*;
import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ServicesImpl implements IService {
    private final IPlayerRepository<Integer, Player> playerRepository;
    private final IGameRepository<Integer, Game> gameRepository;
    private final IScoreRepository<Integer, Score> scoreRepository;
    private final Map<Integer, IObserver> loggedOnes;
    private final Map<Player, Game> gamePlayerMap = new HashMap<>();

    public ServicesImpl(IPlayerRepository<Integer, Player> playerRepository,
                        IGameRepository<Integer, Game> gameRepository,
                        IScoreRepository<Integer, Score> scoreRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.scoreRepository = scoreRepository;
        loggedOnes = new ConcurrentHashMap<>();
    }

    @Override
    public Iterable<DTO> getRanking() {
        List<DTO> list = new ArrayList<>();
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        for (Score score : scores) {
            if (score.getAttempts() == 10 || score.getText() != null) {
                DTO dto = new DTO(score.getPlayer(), score.getTime(), score.getAttempts(), score.getText());
                list.add(dto);
            }
        }
        list.sort(Comparator.comparing(DTO::getGuessings).thenComparing(DTO::getTime));
        return list;
    }

    private Long euclideanDistance(Pair p1, Pair p2) {
        return (long) Math.sqrt(Math.pow(p1.getRow() - p2.getRow(), 2) + Math.pow(p1.getCol() - p2.getCol(), 2));
    }

    private void update(Game game, Player player) throws MyException {
        for (IObserver observer : loggedOnes.values()) {
            observer.updateRanking();
        }
        for (Integer id : loggedOnes.keySet()) {
            if (id.equals(player.getId())) {
                GamePlayer gamePlayer = new GamePlayer(game, player);
                loggedOnes.get(id).updateControl(gamePlayer);
            }
        }
    }

    @Override
    public String guess(Pair player_coordinates) throws MyException {
        // get the game for the player from the map
        Player player = player_coordinates.getPlayer();
        Game game = gamePlayerMap.get(player);
        if (game == null)
            throw new MyException("Player not in game.");
        Pair target = new Pair(new Player(), game.getRow(), game.getCol());
        Long distance = euclideanDistance(player_coordinates, target);
        // pair to string
        String coord = "(" + player_coordinates.getRow() + "," + player_coordinates.getCol() + ") ";
        int attempts = 0;
        Score oldScore = scoreRepository.getLatestScoreForPlayer(player.getId());
        String oldPositions = oldScore.getPositions() == null ? "" : oldScore.getPositions();
        if (distance == 0) {
            // game over
            // add score
            gamePlayerMap.remove(player);
            if (oldScore != null)
                attempts = oldScore.getAttempts() + 1;
            Score score = new Score(oldScore.getId(), game, player, attempts, oldPositions + coord, game.getClue(), oldScore.getTime());
            scoreRepository.update(score);
            update(game, player);
            return "You won!";
        } else {
            attempts = oldScore.getAttempts() + 1;
            if (attempts > 4) {
                gamePlayerMap.remove(player);
                Score score = new Score(oldScore.getId(), game, player, 10, oldPositions + coord, null, oldScore.getTime());
                scoreRepository.update(score);
                update(game, player);
                return "You lost!";
            }
            Score score = new Score(oldScore.getId(), game, player, attempts, oldPositions + coord, null, oldScore.getTime());
            scoreRepository.update(score);
            return distance.toString();
        }
    }

    @Override
    public String setControlGameOver(GamePlayer gamePlayer) throws MyException {
        Player player = gamePlayer.getPlayer();
        Game game = gamePlayer.getGame();
        Score oldScore = scoreRepository.getLatestScoreForPlayer(player.getId());
        String control = "Clue position: (" + game.getRow() + "," + game.getCol() + "), ";
        control += "Number of attempts: " + oldScore.getAttempts() + ", ";
        List<DTO> list = new ArrayList<>();
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        for (Score score : scores) {
            if (score.getAttempts() == 10 || score.getText() != null) {
                DTO dto = new DTO(score.getPlayer(), score.getTime(), score.getAttempts(), score.getText());
                list.add(dto);
            }
        }
        list.sort(Comparator.comparing(DTO::getGuessings).thenComparing(DTO::getTime));
        // find in list the player by time from oldScore
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTime().equals(oldScore.getTime())) {
                index = i+1;
                break;
            }
        }
        control += "Ranking: " + index + " out of " + list.size() + " games finished.";
        return control;
    }


    @Override
    public synchronized Player login(Player player, IObserver iObserver) throws MyException {
        Player toLogin = playerRepository.login(player.getAlias());
        if (toLogin != null) {
            if (loggedOnes.get(toLogin.getId()) != null)
                throw new MyException("User already logged in.");
            loggedOnes.put(toLogin.getId(), iObserver);
            List<Game> games = (List<Game>) gameRepository.getAll();
            int random = (int) (Math.random() * games.size());
            Game game = games.get(random);
            gamePlayerMap.put(toLogin, game);
            Score score = new Score(0, game, toLogin, 0, null, null, LocalTime.now());
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
