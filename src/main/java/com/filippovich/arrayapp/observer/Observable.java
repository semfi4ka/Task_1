package com.filippovich.arrayapp.observer;

import com.filippovich.arrayapp.entity.StringArray;

public interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(StringArray array, String eventType);
}