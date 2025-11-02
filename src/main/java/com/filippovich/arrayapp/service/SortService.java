package com.filippovich.arrayapp.service;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.exception.InvalidArrayException;

public interface SortService {
    StringArray sortByLengthBubble(StringArray array) throws InvalidArrayException;

    StringArray sortByLengthSelection(StringArray array) throws InvalidArrayException;

    StringArray sortByLengthQuick(StringArray array) throws InvalidArrayException;

    void quickSortByLength(String[] arr, int low, int high);

    int partitionByLength(String[] arr, int low, int high);

    StringArray sortAlphabetically(StringArray array) throws InvalidArrayException;

    StringArray sortByLengthDescending(StringArray array) throws InvalidArrayException;
}
