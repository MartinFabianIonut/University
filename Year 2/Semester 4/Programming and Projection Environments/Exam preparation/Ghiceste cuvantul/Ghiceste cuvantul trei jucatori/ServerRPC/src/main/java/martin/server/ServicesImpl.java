package martin.server;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IPlayerRepository;
import app.rest.persistance.repository.IScoreRepository;
import domain.*;
import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;

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
                    if (score.getPlayer_who_guessed().equals(player)
                            && score.getGame().equals(game)) {
                        points += score.getPoints();
                    }
            }
            list.add(new DTO(0, player, "", points));
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
        addScore(new Score(0, game, game.getPlayer1(), game.getPlayer1(), 0, 0, null));
        addScore(new Score(0, game, game.getPlayer2(), game.getPlayer2(), 0, 0, null));
        addScore(new Score(0, game, game.getPlayer3(), game.getPlayer3(), 0, 0, null));
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

    private String getPlayerWord(Player player) {
        // Retrieve the corresponding word for a player in the current game
        if (Objects.equals(player, game.getPlayer1())) {
            return game.getWord1();
        }
        if (Objects.equals(player, game.getPlayer2())) {
            return game.getWord2();
        }
        if (Objects.equals(player, game.getPlayer3())) {
            return game.getWord3();
        }
        return "";
    }

    private String getLettersForPlayer(Player player, List<Score> scores) {
        StringBuilder letters = new StringBuilder();
        for (Score score : scores) {
            // Collect all the guessed letters for a player in the current game
            if (Objects.equals(score.getGame(), game) &&
                    Objects.equals(score.getPlayer_who_proposed(), player) &&
                    score.getGuess() != null) {
                letters.append(score.getGuess());
            }
        }
        return letters.toString();
    }

    private String maskWord(String word, String letters) {
        StringBuilder maskedWord = new StringBuilder(word);
        for (int i = 0; i < word.length(); i++) {
            // Replace the letters that have not been guessed with underscores
            if (!letters.contains(String.valueOf(word.charAt(i)))) {
                maskedWord.setCharAt(i, '_');
            }
        }
        return maskedWord.toString();
    }

    @Override
    public synchronized Iterable<DTO> getAllScores() {
        List<Score> lastScores = new ArrayList<>();
        for (Player player : players.keySet()) {
            // Retrieve the latest score for each player in the current game
            Score lastScore = scoreRepository.getLatestMoveForPlayer(player.getId());
            if (lastScore != null) {
                lastScores.add(lastScore);
            }
        }
        if (lastScores.size() != 3) {
            return null;
        }
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        List<DTO> allScores = new ArrayList<>();
        for (Player player : players.keySet()) {
            String word = getPlayerWord(player);
            Score lastScore = scoreRepository.getLatestMoveForPlayer(player.getId());
            DTO dto = new DTO();
            dto.setPlayer(player);
            String letters = getLettersForPlayer(player, scores);
            String maskedWord = maskWord(word, letters);
            dto.setWord(maskedWord);
            dto.setScore(lastScore.getPoints());
            allScores.add(dto);
        }
        return allScores;
    }

    private boolean scoreExists(List<Score> scores, Game game, Player proposer, Player guesser, String letter) {
        for (Score score : scores) {
            if (Objects.equals(score.getGame(), game) &&
                    Objects.equals(score.getPlayer_who_proposed(), proposer) &&
                    Objects.equals(score.getPlayer_who_guessed(), guesser) &&
                    Objects.equals(score.getGuess(), letter)) {
                return true;
            }
        }
        return false;
    }

    private int countLetterOccurrences(Player player, String letter) {
        String word = getPlayerWord(player);
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (Objects.equals(word.charAt(i), letter.charAt(0))) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean generate(Score score) throws MyException {
        Pair pair = new Pair(score.getPlayer_who_proposed(), score.getGuess());
        int length = guesses.size();
        guesses.putIfAbsent(score.getPlayer_who_guessed(), pair);
        if (guesses.size() == length)
            throw new MyException("Player already made a guess.");
        if (guesses.size() < 3)
            throw new MyException("Not enough players.");
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        for (Player player : guesses.keySet()) {
            Pair playerPair = guesses.get(player);
            Score newScore = new Score(0, game, playerPair.getPlayer(), player, (round / 3) + 1, 0, playerPair.getLetter());
            if (!scoreExists(scores, game, playerPair.getPlayer(), player, playerPair.getLetter())) {
                int count = countLetterOccurrences(playerPair.getPlayer(), playerPair.getLetter());
                newScore.setPoints(count);
                addScore(newScore);
            }
            round++;
        }
        if (round % 3 == 0) {
            guesses.clear();
        }
        if (round > 8) {
            for (IObserver observer : loggedOnes.values()) {
                observer.updateRanking();
            }
        }
        return true;
    }
}
