package com.filippovich.arrayapp.service.impl;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.service.SortService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;

public class SortServiceImpl implements SortService {
    private static final Logger logger = LogManager.getLogger(SortServiceImpl.class);

    @Override
    public StringArray sortByLengthBubble(StringArray array) throws InvalidArrayException {
        logger.debug("Bubble sorting by word length: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] arr = array.getArray();
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].length() > arr[j + 1].length()) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }

        StringArray sortedArray = ArrayFactory.createFromArray(arr);
        logger.debug("Bubble sort completed. Result: {}", sortedArray);
        return sortedArray;
    }

    @Override
    public StringArray sortByLengthSelection(StringArray array) throws InvalidArrayException {
        logger.debug("Selection sorting by word length: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] arr = array.getArray();
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j].length() < arr[minIndex].length()) {
                    minIndex = j;
                }
            }
            String temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }

        StringArray sortedArray = ArrayFactory.createFromArray(arr);
        logger.debug("Selection sort completed. Result: {}", sortedArray);
        return sortedArray;
    }

    @Override
    public StringArray sortByLengthQuick(StringArray array) throws InvalidArrayException {
        logger.debug("Quick sorting by word length: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] arr = array.getArray();
        if (arr.length > 1) {
            quickSortByLength(arr, 0, arr.length - 1);
        }

        StringArray sortedArray = ArrayFactory.createFromArray(arr);
        logger.debug("Quick sort completed. Result: {}", sortedArray);
        return sortedArray;
    }

    @Override
    public void quickSortByLength(String[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByLength(arr, low, high);
            quickSortByLength(arr, low, pivotIndex - 1);
            quickSortByLength(arr, pivotIndex + 1, high);
        }
    }

    @Override
    public int partitionByLength(String[] arr, int low, int high) {
        int pivot = arr[high].length();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].length() <= pivot) {
                i++;
                String temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        String temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    @Override
    public StringArray sortAlphabetically(StringArray array) throws InvalidArrayException {
        logger.debug("Sorting alphabetically: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] arr = array.getArray();
        Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);

        StringArray sortedArray = ArrayFactory.createFromArray(arr);
        logger.debug("Alphabetical sort completed. Result: {}", sortedArray);
        return sortedArray;
    }

    @Override
    public StringArray sortByLengthDescending(StringArray array) throws InvalidArrayException {
        logger.debug("Sorting by length descending: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] arr = array.getArray();
        Arrays.sort(arr, Comparator.comparingInt(String::length).reversed());

        StringArray sortedArray = ArrayFactory.createFromArray(arr);
        logger.debug("Length descending sort completed. Result: {}", sortedArray);
        return sortedArray;
    }
}