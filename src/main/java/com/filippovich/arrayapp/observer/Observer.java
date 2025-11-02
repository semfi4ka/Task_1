package com.filippovich.arrayapp.observer;

import com.filippovich.arrayapp.entity.StringArray;

public interface Observer {
    void handleEvent(StringArray array, String eventType);
}