package martin.service;

import domain.Player;

public interface IObserver {
     void updateRanking() throws MyException;
     void updateControl(Player player) throws MyException;
}
