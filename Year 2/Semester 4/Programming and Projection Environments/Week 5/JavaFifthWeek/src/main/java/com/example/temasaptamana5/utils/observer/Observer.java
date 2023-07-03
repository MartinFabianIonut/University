package com.example.temasaptamana5.utils.observer;

import com.example.temasaptamana5.utils.utils.Event;

public interface Observer<E extends Event> {
    void update(E event);
}
