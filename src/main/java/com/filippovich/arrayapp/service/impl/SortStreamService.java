package com.filippovich.arrayapp.service.impl;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.service.SortService;
import com.filippovich.arrayapp.comparator.impl.StringArrayComparatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class SortStreamService implements SortService {
    private static final Logger logger = LogManager.getLogger(SortStreamService.class);
    private final StringArrayComparatorImpl comparator = new StringArrayComparatorImpl();

    @Override
    public StringArray sortByLengthBubble(StringArray array) throws InvalidArrayException {
        logger.debug("Stream bubble sort by length: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byLength().compare(arr1, arr2);
                })
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream bubble sorted by length: {}", result);
        return result;
    }

    @Override
    public StringArray sortByLengthSelection(StringArray array) throws InvalidArrayException {
        logger.debug("Stream selection sort by length: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .parallel()
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byLength().compare(arr1, arr2);
                })
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream selection sorted by length: {}", result);
        return result;
    }

    @Override
    public StringArray sortByLengthQuick(StringArray array) throws InvalidArrayException {
        logger.debug("Stream quick sort by length: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .parallel()
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    int lengthCompare = comparator.byLength().compare(arr1, arr2);
                    if (lengthCompare != 0) {
                        return lengthCompare;
                    }
                    // Если длины равны, сравниваем по алфавиту
                    return comparator.byFirstElement().compare(arr1, arr2);
                })
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream quick sorted by length: {}", result);
        return result;
    }

    @Override
    public void quickSortByLength(String[] arr, int low, int high) {
        logger.debug("Quick sort by length for array portion [{}, {}]", low, high);

        if (low < high) {
            String[] subArray = Arrays.copyOfRange(arr, low, high + 1);

            // Используем компаратор для сортировки подмассива
            String[] sortedSubArray = Arrays.stream(subArray)
                    .sorted((s1, s2) -> {
                        StringArray arr1 = new StringArray(new String[]{s1});
                        StringArray arr2 = new StringArray(new String[]{s2});
                        return comparator.byLength().compare(arr1, arr2);
                    })
                    .toArray(String[]::new);

            System.arraycopy(sortedSubArray, 0, arr, low, sortedSubArray.length);
        }
    }

    @Override
    public int partitionByLength(String[] arr, int low, int high) {
        logger.debug("Partition by length from {} to {}", low, high);

        String pivot = arr[high];
        int pivotLength = pivot.length();

        // Используем компаратор для подсчета элементов
        long countLessOrEqual = Arrays.stream(arr, low, high)
                .filter(word -> {
                    StringArray wordArray = new StringArray(new String[]{word});
                    StringArray pivotArray = new StringArray(new String[]{pivot});
                    return comparator.byLength().compare(wordArray, pivotArray) <= 0;
                })
                .count();

        return low + (int) countLessOrEqual - 1;
    }

    @Override
    public StringArray sortAlphabetically(StringArray array) throws InvalidArrayException {
        logger.debug("Stream alphabetical sort: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byFirstElement().compare(arr1, arr2);
                })
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream alphabetically sorted: {}", result);
        return result;
    }

    @Override
    public StringArray sortByLengthDescending(StringArray array) throws InvalidArrayException {
        logger.debug("Stream sort by length descending: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byLength().compare(arr2, arr1); // Обратный порядок
                })
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream sorted by length descending: {}", result);
        return result;
    }

    // Дополнительные методы с использованием других компараторов
    public StringArray sortByLastElement(StringArray array) throws InvalidArrayException {
        logger.debug("Stream sort by last element: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byLastElement().compare(arr1, arr2);
                })
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream sorted by last element: {}", result);
        return result;
    }

    public StringArray sortByAlphabeticalOrder(StringArray array) throws InvalidArrayException {
        logger.debug("Stream sort by alphabetical order: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byAlphabeticalOrder().compare(arr1, arr2);
                })
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream sorted by alphabetical order: {}", result);
        return result;
    }
}