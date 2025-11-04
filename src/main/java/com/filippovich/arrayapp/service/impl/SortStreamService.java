package com.filippovich.arrayapp.service.impl;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.service.SortService;
import com.filippovich.arrayapp.comparator.impl.StringArrayComparatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

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

        String[] arr = array.getArray().clone();
        int n = arr.length;

        IntStream.range(0, n - 1)
                .forEach(i -> IntStream.range(0, n - i - 1)
                        .forEach(j -> {
                            if (arr[j].length() > arr[j + 1].length()) {
                                String temp = arr[j];
                                arr[j] = arr[j + 1];
                                arr[j + 1] = temp;
                            }
                        }));

        StringArray result = ArrayFactory.createFromArray(arr);
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

        String[] arr = array.getArray().clone();
        int n = arr.length;

        IntStream.range(0, n - 1)
                .forEach(i -> {
                    final int currentI = i;
                    int minIndex = IntStream.range(currentI, n)
                            .reduce(currentI, (min, j) ->
                                    arr[j].length() < arr[min].length() ? j : min);

                    String temp = arr[minIndex];
                    arr[minIndex] = arr[currentI];
                    arr[currentI] = temp;
                });

        StringArray result = ArrayFactory.createFromArray(arr);
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
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
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

            String[] sortedSubArray = Arrays.stream(subArray)
                    .sorted(Comparator.comparingInt(String::length))
                    .toArray(String[]::new);

            System.arraycopy(sortedSubArray, 0, arr, low, sortedSubArray.length);
        }
    }

    @Override
    public int partitionByLength(String[] arr, int low, int high) {
        logger.debug("Partition by length from {} to {}", low, high);

        String pivot = arr[high];
        int pivotLength = pivot.length();

        String[] lessOrEqual = Arrays.stream(arr, low, high)
                .filter(word -> word.length() <= pivotLength)
                .toArray(String[]::new);

        String[] greater = Arrays.stream(arr, low, high)
                .filter(word -> word.length() > pivotLength)
                .toArray(String[]::new);

        System.arraycopy(lessOrEqual, 0, arr, low, lessOrEqual.length);
        arr[low + lessOrEqual.length] = pivot;
        System.arraycopy(greater, 0, arr, low + lessOrEqual.length + 1, greater.length);

        return low + lessOrEqual.length;
    }

    @Override
    public StringArray sortAlphabetically(StringArray array) throws InvalidArrayException {
        logger.debug("Stream alphabetical sort: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted(String.CASE_INSENSITIVE_ORDER)
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
                .sorted(Comparator.comparingInt(String::length)
                        .reversed()
                        .thenComparing(Comparator.naturalOrder()))
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream sorted by length descending: {}", result);
        return result;
    }

    public StringArray sortByLastElement(StringArray array) throws InvalidArrayException {
        logger.debug("Stream sort by last element: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted((s1, s2) -> {
                    char last1 = s1.isEmpty() ? ' ' : s1.charAt(s1.length() - 1);
                    char last2 = s2.isEmpty() ? ' ' : s2.charAt(s2.length() - 1);
                    return Character.compare(last1, last2);
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
                .sorted(Comparator.naturalOrder())
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream sorted by alphabetical order: {}", result);
        return result;
    }

    public StringArray sortByVowelCount(StringArray array) throws InvalidArrayException {
        logger.debug("Stream sort by vowel count: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted(Comparator.comparingInt(this::countVowels)
                        .thenComparing(Comparator.naturalOrder()))
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream sorted by vowel count: {}", result);
        return result;
    }

    public StringArray sortByConsonantCount(StringArray array) throws InvalidArrayException {
        logger.debug("Stream sort by consonant count: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted(Comparator.comparingInt(this::countConsonants)
                        .thenComparing(Comparator.naturalOrder()))
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream sorted by consonant count: {}", result);
        return result;
    }

    private int countVowels(String word) {
        return (int) word.toLowerCase().chars()
                .filter(c -> "aeiouаеёиоуыэюя".indexOf(c) != -1)
                .count();
    }

    private int countConsonants(String word) {
        return (int) word.toLowerCase().chars()
                .filter(c -> Character.isLetter(c) && "aeiouаеёиоуыэюя".indexOf(c) == -1)
                .count();
    }

    public StringArray sortWithCustomComparator(StringArray array,
                                                Comparator<String> customComparator) throws InvalidArrayException {
        logger.debug("Stream sort with custom comparator: {}", array);

        if (array.isEmpty()) {
            logger.debug("Empty array - nothing to sort");
            return array;
        }

        String[] sorted = Arrays.stream(array.getArray())
                .sorted(customComparator)
                .toArray(String[]::new);

        StringArray result = ArrayFactory.createFromArray(sorted);
        logger.debug("Stream sorted with custom comparator: {}", result);
        return result;
    }
}