package com.filippovich.arrayapp.repository.impl;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.observer.ObserverManager;
import com.filippovich.arrayapp.repository.Specification;
import com.filippovich.arrayapp.repository.StringArrayRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class StringArrayRepositoryImpl implements StringArrayRepository {
    private static final Logger logger = LogManager.getLogger(StringArrayRepositoryImpl.class);
    private static final StringArrayRepositoryImpl instance = new StringArrayRepositoryImpl();

    private final List<StringArray> storage = new ArrayList<>();
    private final ObserverManager observerManager = new ObserverManager();

    private StringArrayRepositoryImpl() {
        logger.info("StringArrayRepositoryImpl singleton created");
    }

    public static StringArrayRepositoryImpl getInstance() {
        return instance;
    }

    public void addObserver(com.filippovich.arrayapp.observer.Observer o) {
        observerManager.addObserver(o);
    }

    public void removeObserver(com.filippovich.arrayapp.observer.Observer o) {
        observerManager.removeObserver(o);
    }

    private void notifyObservers(StringArray array, String eventType) {
        observerManager.notifyObservers(array, eventType);
    }

    @Override
    public void add(StringArray stringArray) {
        if (stringArray != null && stringArray.getId() != null) {
            storage.add(stringArray);
            notifyObservers(stringArray, "ADD");
            logger.debug("Array added to repository: {}", stringArray.getId());
        }
    }

    @Override
    public boolean remove(StringArray stringArray) {
        if (stringArray == null) {
            return false;
        }

        UUID id = stringArray.getId();
        if (id == null) {
            return false;
        }

        Iterator<StringArray> iterator = storage.iterator();
        while (iterator.hasNext()) {
            StringArray element = iterator.next();

            if (element != null && id.equals(element.getId())) {
                iterator.remove();
                notifyObservers(element, "REMOVE");
                logger.debug("Array removed from repository: {}", id);
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        int initialSize = storage.size();
        logger.info("Starting repository clearance. Current size: {}", initialSize);

        if (initialSize == 0) {
            logger.info("Repository is already empty - nothing to clear");
            return;
        }

        for (StringArray array : new ArrayList<>(storage)) {
            notifyObservers(array, "REMOVE");
        }

        storage.clear();
        logger.info("Repository cleared successfully. Removed {} arrays", initialSize);
    }

    @Override
    public List<StringArray> query(Specification spec) {
        return storage.stream()
                .filter(spec::specified)
                .collect(Collectors.toList());
    }

    @Override
    public List<StringArray> getAll() {
        return new ArrayList<>(storage);
    }

    public int getObserverCount() {
        return observerManager.getObserverCount();
    }

    public void clearObservers() {
        observerManager.clearObservers();
    }
}