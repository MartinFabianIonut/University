package com.example.temasaptamana5.utils.observer;

import com.example.temasaptamana5.utils.utils.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notifyObservers(E event);
}

