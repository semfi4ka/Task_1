package com.filippovich.arrayapp.warehouse;

public interface ArrayStatistics {
    double getAverageLength();

    int getTotalCharacters();

    int getMaxLength();

    int getMinLength();

    int getWordCount();

    @Override
    String toString();
}
