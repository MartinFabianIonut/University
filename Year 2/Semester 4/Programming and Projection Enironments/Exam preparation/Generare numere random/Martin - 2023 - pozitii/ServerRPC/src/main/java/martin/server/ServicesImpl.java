package martin.server;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IPlayerRepository;
import app.rest.persistance.repository.IScoreRepository;
import domain.*;
import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


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
            if (score.getGameOver() == 1) {
                DTO dto = new DTO(score.getPlayer(), score.getTime(), score.getTotalSum());
                list.add(dto);
            }
        }
        list.sort(Comparator.comparing(DTO::getTotalSum).thenComparing(DTO::getTime).reversed());
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
    public String setControlGameOver(Player player) {
        Score oldScore = scoreRepository.getLatestScoreForPlayer(player.getId());
        List<DTO> list = (List<DTO>) getRanking();
        // find in list the player by time from oldScore
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTime().equals(oldScore.getTime())) {
                index = i + 1;
                break;
            }
        }
        String control = "";
        control += "GAME OVER!\nTotal sum = " + oldScore.getTotalSum() +
                ", Ranking: " + index + " out of " + list.size() + " games finished.";
        return control;
    }

    @Override
    public Game getGame(Player player) {
        return gamePlayerMap.get(player);
    }

    @Override
    public Integer generate(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MyException {
        int random = (int) (Math.random() * 3 + 1);
        Score oldScore = scoreRepository.getLatestScoreForPlayer(player.getId());
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        // filter scores by player and game
        scores = scores.stream()
                .filter(score -> score.getPlayer().getId().equals(player.getId())
                        && score.getGame().getId().equals(gamePlayerMap.get(player).getId())
                        && score.getTime().equals(oldScore.getTime()))
                .collect(Collectors.toList());
        int gameOver = 0;
        if (scores.size() == 3) {
            // add final score
            gameOver = 1;
        }

        int oldPosition = oldScore.getPosition();
        int newPosition, sum = 0;
        if (oldPosition + random > 5) {
            newPosition = (oldPosition + random) % 5;
            sum += 5;
        } else {
            newPosition = oldPosition + random;
        }
        Game game = gamePlayerMap.get(player);
        boolean alreadyVisited = false;
        for (Score score : scores) {
            if (score.getPosition() == newPosition) {
                alreadyVisited = true;
                break;
            }
        }
        if (!alreadyVisited) {
            String method = "getValue" + newPosition;
            Method myMethod = game.getClass().getMethod(method);
            assert false;
            Integer value = (Integer) myMethod.invoke(game);
            sum -= value;
        }
        Score score = new Score(0, game, player,
                newPosition, sum, oldScore.getTotalSum()+sum, gameOver, oldScore.getTime());
        scoreRepository.add(score);
        if (gameOver == 1) {
            gamePlayerMap.remove(player);
            update(player);
        }
        return newPosition;
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
            Score score = new Score(0, game, toLogin, 0, 50, 50, 0, LocalDateTime.now());
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
