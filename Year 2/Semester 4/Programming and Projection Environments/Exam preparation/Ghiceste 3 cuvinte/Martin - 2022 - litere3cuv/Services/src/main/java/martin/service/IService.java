package martin.service;


import domain.*;

import java.lang.reflect.InvocationTargetException;

public interface IService {
     Player login(Player game, IObserver iObserver) throws MyException;
     void logout(Player game, IObserver iObserver) throws MyException;
     Iterable<DTO> getRanking() throws MyException;
     String guess(Pair player_coordinates) throws MyException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
     String setControlGameOver(Player player) throws MyException;
     Game getGame(Player player) throws MyException;
}
