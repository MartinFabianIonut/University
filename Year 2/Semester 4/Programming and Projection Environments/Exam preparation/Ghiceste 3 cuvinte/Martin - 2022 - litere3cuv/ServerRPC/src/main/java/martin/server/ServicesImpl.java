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
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        return scores.stream()
                .filter(score -> score.getRound() == 3)
                .map(score -> new DTO(score.getPlayer(), score.getTime(), score.getTotalPoints()))
                .sorted(Comparator.comparing(DTO::getTotalPoints).thenComparing(DTO::getTime).reversed())
                .toList();
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
    public String guess(Pair player_letter) throws MyException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // get the game for the player from the map
        Player player = player_letter.getPlayer();
        String word = player_letter.getWord();
        Game game = gamePlayerMap.get(player);
        Score oldScore = scoreRepository.getLatestScoreForPlayer(player.getId());
        int oldRound = oldScore.getRound();
        if (oldRound == 3)
            return "Game over!";
        int oldPoints = oldScore.getTotalPoints();
        int position = 0;
        int maxPoints = 0, newRound = oldRound + 1;

        Method myMethod;
        String method;
        assert false;
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        // filter the scores for the current player
        scores = scores.stream()
                .filter(score -> score.getTime().equals(oldScore.getTime()))
                .filter(score -> score.getPlayer().getId().equals(player.getId()))
                .toList();
        // get the max points for the current round
        List<Integer> positions = new ArrayList<>();
        for (Score score : scores) {
            if (score.getWord().equals(word))
                return "You already guessed this word!";
            if (score.getPositionGuessedWord() != 0)
                positions.add(score.getPositionGuessedWord() - 1); // -1 because the position is 1,2,3 and the index is 0,1,2
        }

        for (int i = 0; i < 3; i++) {
            if (!positions.contains(i) || positions.isEmpty()) {
                // iterate through the words and get the number of letters that are in the word at correct position
                int count = 0;
                method = "getWord" + (i + 1);
                myMethod = game.getClass().getMethod(method);
                String wordToCompare = (String) myMethod.invoke(game);
                for (int j = 0; j < wordToCompare.length(); j++) {
                    if (word.length() > j)
                        if (word.charAt(j) == wordToCompare.charAt(j))
                            count++;
                }
                if (word.equals(wordToCompare)) {
                    position = i + 1;
                    maxPoints = count;
                    break;
                }
                if (count > maxPoints)
                    maxPoints = count;
            }
        }

        Score newScore = new Score(0, game, player, newRound, position, word,
                maxPoints, oldPoints + maxPoints, oldScore.getTime());
        scoreRepository.add(newScore);
        if (oldRound == 2) {
            update(player);
        }
        if (position != 0)
            return "Cuvant ghicit, ai primit " + maxPoints + " puncte!";
        if (maxPoints != 0)
            return "Cuvantul nu a fost ghicit, dar ai primit " + maxPoints + " puncte!";
        return "Cuvantul nu a fost ghicit, ai primit 0 puncte!";
    }

    @Override
    public Game getGame(Player player) {
        return gamePlayerMap.get(player);
    }

    @Override
    public String setControlGameOver(Player player) {
        Game game = gamePlayerMap.get(player);
        gamePlayerMap.remove(player);
        Score oldScore = scoreRepository.getLatestScoreForPlayer(player.getId());
        List<DTO> list = new ArrayList<>();
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        for (Score score : scores) {
            if (score.getRound() == 3) {
                DTO dto = new DTO(score.getPlayer(), score.getTime(), score.getTotalPoints());
                list.add(dto);
            }
        }
        list.sort(Comparator.comparing(DTO::getTotalPoints).thenComparing(DTO::getTime).reversed());
        // find in list the player by time from oldScore
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTime().equals(oldScore.getTime())) {
                index = i + 1;
                break;
            }
        }
        return "Words: " + game.getWord1() + ", " + game.getWord2() + ", " + game.getWord3() + "\n" +
                "You are on position " + index + " with " + oldScore.getTotalPoints() + " points.";
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
            Score score = new Score(0, game, toLogin, 0, 0, "", 0, 0, LocalDateTime.now());
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
