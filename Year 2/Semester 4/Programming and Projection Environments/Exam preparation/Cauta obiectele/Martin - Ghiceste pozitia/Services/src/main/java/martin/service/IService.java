package martin.service;


import domain.*;

import java.lang.reflect.InvocationTargetException;

public interface IService {
     Player login(Player game, IObserver iObserver) throws MyException;
     void logout(Player game, IObserver iObserver) throws MyException;
     Iterable<DTO> getAllScores() throws MyException;
     boolean generate(Score score) throws MyException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;
     void addScore(Score score) throws MyException;
     boolean startGame(Game game) throws MyException;
     Game getGame() throws MyException;
     Iterable<DTO> getRanking() throws MyException;
}
