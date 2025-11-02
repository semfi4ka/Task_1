package com.filippovich.arrayapp.warehouse.impl;

import com.filippovich.arrayapp.warehouse.ArrayStatistics;

import java.util.StringJoiner;

public class ArrayStatisticsImpl implements ArrayStatistics {

    private double averageLength;
    private int totalCharacters;
    private int maxLength;
    private int minLength;
    private int wordCount;

    public ArrayStatisticsImpl(double averageLength, int totalCharacters, int maxLength, int minLength, int wordCount) {
        this.averageLength = averageLength;
        this.totalCharacters = totalCharacters;
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.wordCount = wordCount;
    }

    @Override
    public double getAverageLength() {
        return averageLength;
    }

    @Override
    public int getTotalCharacters() {
        return totalCharacters;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public int getMinLength() {
        return minLength;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ArrayStatisticsImpl.class.getSimpleName() + "[", "]")
                .add("avg=" + averageLength)
                .add("sum=" + totalCharacters)
                .add("max=" + maxLength)
                .add("min=" + minLength)
                .add("count=" + wordCount)
                .toString();
    }
}