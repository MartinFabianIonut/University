package martin.service;


import domain.*;

public interface IService {
     Player login(Player game, IObserver iObserver) throws MyException;
     void logout(Player game, IObserver iObserver) throws MyException;
     Iterable<DTO> getRanking() throws MyException;
     String guess(Pair player_coordinates) throws MyException;
     String setControlGameOver(Player player) throws MyException;
}
