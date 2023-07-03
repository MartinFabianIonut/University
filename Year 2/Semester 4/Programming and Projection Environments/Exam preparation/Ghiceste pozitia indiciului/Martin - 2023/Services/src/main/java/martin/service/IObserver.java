package martin.service;

import domain.GamePlayer;

public interface IObserver {
     void updateRanking() throws MyException;
     void updateControl(GamePlayer gamePlayer) throws MyException;
}
