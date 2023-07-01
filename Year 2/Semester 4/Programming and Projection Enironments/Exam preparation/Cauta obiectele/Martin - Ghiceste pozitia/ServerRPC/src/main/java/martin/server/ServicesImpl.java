package martin.server;

import domain.*;
import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IScoreRepository;
import app.rest.persistance.repository.IPlayerRepository;
import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ServicesImpl implements IService {
    private final IPlayerRepository<Integer, Player> playerRepository;
    private final IGameRepository<Integer, Game> gameRepository;
    private final IScoreRepository<Integer, Score> scoreRepository;
    private final Map<Integer, IObserver> loggedOnes;
    private Game game;
    private final Map<Player, String> players = new HashMap<>();
    private final Map<Player, Pair> guesses = new HashMap<>();

    private Integer round = 0;

    public ServicesImpl(IPlayerRepository<Integer, Player> playerRepository,
                        IGameRepository<Integer, Game> gameRepository,
                        IScoreRepository<Integer, Score> scoreRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.scoreRepository = scoreRepository;
        loggedOnes = new ConcurrentHashMap<>();
        game = new Game();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public Iterable<DTO> getRanking() {
        List<DTO> list = new ArrayList<>();
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        for (Player player : players.keySet()) {
            int points = 0;
            for (Score score : scores) {
                if (score.getPlayer_who_guessed() != null)
                    if(score.getPlayer_who_guessed().equals(player)
                    && score.getGame().equals(game)) {
                        points += score.getPoints();
                    }
            }
            list.add(new DTO(player, "", points));
        }
        list.sort(Comparator.comparing(DTO::getScore).reversed());
        return list;
    }

    @Override
    public synchronized void addScore(Score score) throws MyException {
        if (!scoreRepository.update(score))
            scoreRepository.add(score);
        for (IObserver observer : loggedOnes.values()) {
            observer.update(score);
        }
    }

    private void addGame(Game game) throws MyException {
        gameRepository.add(game);
        addScore(new Score(0, game, game.getPlayer1(), game.getPlayer1(), 0, 0, -1));
        addScore(new Score(0, game, game.getPlayer2(), game.getPlayer2(), 0, 0, -1));
        addScore(new Score(0, game, game.getPlayer3(), game.getPlayer3(), 0, 0, -1));
    }

    @Override
    public boolean startGame(Game game) throws MyException {
        players.putIfAbsent(game.getPlayer1(), game.getWord1());
        if (players.size() == 3) {
            Game newGame = new Game();
            for (Player player : players.keySet()) {
                if (newGame.getPlayer1() == null) {
                    newGame.setPlayer1(player);
                    newGame.setWord1(players.get(player));
                    continue;
                }
                if (newGame.getPlayer2() == null) {
                    newGame.setPlayer2(player);
                    newGame.setWord2(players.get(player));
                    continue;
                }
                if (newGame.getPlayer3() == null) {
                    newGame.setPlayer3(player);
                    newGame.setWord3(players.get(player));
                }
            }
            addGame(newGame);
            this.game = newGame;
            return true;
        }
        return false;
    }

    @Override
    public synchronized Player login(Player game, IObserver iObserver) throws MyException {
        Player toLogin = playerRepository.login(game.getUsername(), game.getPassword());
        if (toLogin != null) {
            if (loggedOnes.get(toLogin.getId()) != null)
                throw new MyException("User already logged in.");
            loggedOnes.put(toLogin.getId(), iObserver);
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

    @Override
    public synchronized Iterable<DTO> getAllScores() {
        List<Score> lastScores = new ArrayList<>();
        for (Player player : players.keySet()) {
            Score lastScore = scoreRepository.getLatestMoveForPlayer(player.getId());
            if (lastScore != null) {
                lastScores.add(lastScore);
            }
        }
        if (lastScores.size() != 3) {
            return null;
        }
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        List<DTO> dtos = new ArrayList<>();

        // find the word in game for the player
        String word = "";
        for (Player player : players.keySet()) {
            if (Objects.equals(player, game.getPlayer1())) {
                word = game.getWord1();
            }
            if (Objects.equals(player, game.getPlayer2())) {
                word = game.getWord2();
            }
            if (Objects.equals(player, game.getPlayer3())) {
                word = game.getWord3();
            }
            Score lastScore = scoreRepository.getLatestMoveForPlayer(player.getId());
            DTO dto = new DTO();
            dto.setPlayer(player);
            List<Integer>positions = new ArrayList<>();
            // find the letters in scores for the player and the game
            for (Score score1 : scores) {
                if (Objects.equals(score1.getGame(), game) && score1.getGuess() != null
                        && Objects.equals(score1.getPlayer_who_proposed(), player)) {
                    positions.add(score1.getGuess());
                }
            }
            System.out.println(player);
            System.out.println(positions);
            // replace the positions that are not in the word with _
            StringBuilder stringBuilder = new StringBuilder(word);
            for (int i = 0; i < word.length(); i++) {
                if (!positions.contains(i)) {
                    stringBuilder.replace(i, i + 1, "_");
                }
                else {
                    if (word.charAt(i) == '1')
                        stringBuilder.replace(i, i + 1, "O");
                    else if (word.charAt(i-1) == '1' || word.charAt(i+1) == '1')
                        stringBuilder.replace(i, i + 1, "N");
                    else {
                        stringBuilder.replace(i, i + 1, "C");
                    }
                }
            }
            word = stringBuilder.toString();
            dto.setGuess(word);
            dto.setScore(lastScore.getPoints());
            dtos.add(dto);
        }
        return dtos;
    }

    private Integer getPoints(Player player, int method_nr) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int count = 0;
        String method = "getWord" + method_nr;
        // call method for game
        Method myMethod = game.getClass().getMethod(method);
        String word = (String) myMethod.invoke(game);
        for (int i = 0; i < word.length(); i++) {
            // player guessed the position of the object
            // if the letter is 1 and the guess is the position, he gets 7 points
            // if the letter is 0 and the next or previous letter is 1, he gets 3 points
            // else he gets 0 points
            if (guesses.get(player).getGuess() == i) {
                if (word.charAt(i) == '1') {
                    count += 7;
                } else {
                    if (i > 0 && word.charAt(i - 1) == '1') {
                        count += 3;
                    }
                    if (i < word.length() - 1 && word.charAt(i + 1) == '1') {
                        count += 3;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public boolean generate(Score score) throws MyException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pair pair = new Pair(score.getPlayer_who_proposed(), score.getGuess());
        int length = guesses.size();
        guesses.putIfAbsent(score.getPlayer_who_guessed(), pair);
        if (guesses.size() == length)
            throw new MyException("Player already made a guess.");
        if (guesses.size() < 3)
            throw new MyException("Not enough players.");
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        for (Player player : guesses.keySet()) {
            Score newScore = new Score(0, game, guesses.get(player).getPlayer(), player, (round / 3) + 1, 0, guesses.get(player).getGuess());
            // number of appearances of the letter in the word
            int count = 0;
            boolean found = false;
            for (Score score1 : scores) {
                if (Objects.equals(score1.getGame(), game)
                        && Objects.equals(score1.getPlayer_who_proposed(), guesses.get(player).getPlayer())
                        && Objects.equals(score1.getPlayer_who_guessed(), player)
                        && Objects.equals(score1.getGuess(), guesses.get(player).getGuess())) {
                    found = true;
                }
            }
            if (!found) {
                if (Objects.equals(guesses.get(player).getPlayer(), game.getPlayer1())) {
                    count += getPoints(player, 1);
                }
                if (Objects.equals(guesses.get(player).getPlayer(), game.getPlayer2())) {
                    count += getPoints(player, 2);
                }
                if (Objects.equals(guesses.get(player).getPlayer(), game.getPlayer3())) {
                    count += getPoints(player, 3);
                }
            }
            newScore.setPoints(count);
            addScore(newScore);
            round++;
        }
        if (round % 3 == 0)
            guesses.clear();
        if (round > 8) {
            for (IObserver observer : loggedOnes.values()) {
                observer.updateRanking();
            }
        }
        return true;
    }

}
