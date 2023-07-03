package martin.service;

import domain.Score;

public interface IObserver {
     void update(Score score) throws MyException;
     void updateRanking() throws MyException;
}
