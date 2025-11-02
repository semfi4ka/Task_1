package com.filippovich.arrayapp.observer;

import com.filippovich.arrayapp.entity.StringArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ObserverManager implements Observable {
    private static final Logger logger = LogManager.getLogger(ObserverManager.class);
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
            logger.debug("Observer added: {}", o.getClass().getSimpleName());
        }
    }

    @Override
    public void removeObserver(Observer o) {
        if (observers.remove(o)) {
            logger.debug("Observer removed: {}", o.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers(StringArray array, String eventType) {
        if (array == null || eventType == null) {
            logger.warn("Cannot notify observers: array or eventType is null");
            return;
        }

        logger.debug("Notifying {} observers about event: {} for array ID: {}",
                observers.size(), eventType, array.getId());

        for (Observer observer : observers) {
            try {
                observer.handleEvent(array, eventType);
                logger.trace("Successfully notified observer: {}", observer.getClass().getSimpleName());
            } catch (Exception e) {
                logger.error("Error notifying observer {}: {}",
                        observer.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
    }

    public int getObserverCount() {
        return observers.size();
    }

    public void clearObservers() {
        logger.info("Clearing all observers. Current count: {}", observers.size());
        observers.clear();
    }

    public List<Class<?>> getObserverClasses() {
        return observers.stream()
                .map(Observer::getClass)
                .collect(java.util.stream.Collectors.toList());
    }
}