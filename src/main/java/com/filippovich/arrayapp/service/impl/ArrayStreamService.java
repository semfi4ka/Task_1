package com.filippovich.arrayapp.service.impl;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.service.ArrayService;
import com.filippovich.arrayapp.comparator.impl.StringArrayComparatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class ArrayStreamService implements ArrayService {
    private static final Logger logger = LogManager.getLogger(ArrayStreamService.class);
    private final StringArrayComparatorImpl comparator = new StringArrayComparatorImpl();

    @Override
    public String findShortestWord(StringArray array) {
        logger.debug("Finding shortest word using Stream API in array: {}", array);

        // Создаем временный StringArray для каждого слова и используем компаратор по длине
        String result = Arrays.stream(array.getArray())
                .min((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byLength().compare(arr1, arr2);
                })
                .orElse("");

        logger.debug("Shortest word found: '{}'", result);
        return result;
    }

    @Override
    public String findLongestWord(StringArray array) {
        logger.debug("Finding longest word using Stream API in array: {}", array);

        String result = Arrays.stream(array.getArray())
                .max((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byLength().compare(arr1, arr2);
                })
                .orElse("");

        logger.debug("Longest word found: '{}'", result);
        return result;
    }

    @Override
    public double calculateAverageLength(StringArray array) {
        logger.debug("Calculating average word length using Stream API in array: {}", array);

        double result = Arrays.stream(array.getArray())
                .mapToInt(String::length)
                .average()
                .orElse(0.0);

        logger.debug("Average word length: {}", result);
        return result;
    }

    @Override
    public int calculateTotalCharacters(StringArray array) {
        logger.debug("Calculating total characters using Stream API in array: {}", array);

        int result = Arrays.stream(array.getArray())
                .mapToInt(String::length)
                .sum();

        logger.debug("Total characters: {}", result);
        return result;
    }

    @Override
    public int countWordsLongerThan(StringArray array, int minLength) {
        logger.debug("Counting words longer than {} using Stream API in array: {}", minLength, array);

        long result = Arrays.stream(array.getArray())
                .filter(word -> word.length() > minLength)
                .count();

        logger.debug("Words longer than {}: {}", minLength, result);
        return (int) result;
    }

    @Override
    public int countWordsShorterThan(StringArray array, int maxLength) {
        logger.debug("Counting words shorter than {} using Stream API in array: {}", maxLength, array);

        long result = Arrays.stream(array.getArray())
                .filter(word -> word.length() < maxLength)
                .count();

        logger.debug("Words shorter than {}: {}", maxLength, result);
        return (int) result;
    }

    @Override
    public StringArray replaceWords(StringArray array, String oldWord, String newWord) throws InvalidArrayException {
        logger.debug("Replacing words using Stream API in array: {}, '{}' -> '{}'", array, oldWord, newWord);

        String[] result = Arrays.stream(array.getArray())
                .map(word -> word.equals(oldWord) ? newWord : word)
                .toArray(String[]::new);

        StringArray replacedArray = ArrayFactory.createFromArray(result);
        logger.debug("Words replaced successfully. Result: {}", replacedArray);
        return replacedArray;
    }

    @Override
    public StringArray replaceWordsByLength(StringArray array, int targetLength, String newWord) throws InvalidArrayException {
        logger.debug("Replacing words by length using Stream API in array: {}, length {} -> '{}'",
                array, targetLength, newWord);

        String[] result = Arrays.stream(array.getArray())
                .map(word -> word.length() == targetLength ? newWord : word)
                .toArray(String[]::new);

        StringArray replacedArray = ArrayFactory.createFromArray(result);
        logger.debug("Words replaced by length successfully. Result: {}", replacedArray);
        return replacedArray;
    }

    @Override
    public String findFirstAlphabetically(StringArray array) {
        logger.debug("Finding first word alphabetically using Stream API in array: {}", array);

        String result = Arrays.stream(array.getArray())
                .min((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byFirstElement().compare(arr1, arr2);
                })
                .orElse("");

        logger.debug("First word alphabetically: '{}'", result);
        return result;
    }

    @Override
    public String findLastAlphabetically(StringArray array) {
        logger.debug("Finding last word alphabetically using Stream API in array: {}", array);

        String result = Arrays.stream(array.getArray())
                .max((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byFirstElement().compare(arr1, arr2);
                })
                .orElse("");

        logger.debug("Last word alphabetically: '{}'", result);
        return result;
    }

    @Override
    public int countWordsStartingWith(StringArray array, char letter) {
        logger.debug("Counting words starting with '{}' using Stream API in array: {}", letter, array);

        long result = Arrays.stream(array.getArray())
                .filter(word -> !word.isEmpty() &&
                        Character.toLowerCase(word.charAt(0)) == Character.toLowerCase(letter))
                .count();

        logger.debug("Words starting with '{}': {}", letter, result);
        return (int) result;
    }

    @Override
    public int countWordsEndingWith(StringArray array, char letter) {
        logger.debug("Counting words ending with '{}' using Stream API in array: {}", letter, array);

        long result = Arrays.stream(array.getArray())
                .filter(word -> !word.isEmpty() &&
                        Character.toLowerCase(word.charAt(word.length() - 1)) == Character.toLowerCase(letter))
                .count();

        logger.debug("Words ending with '{}': {}", letter, result);
        return (int) result;
    }

    // Дополнительные методы с использованием компаратора для StringArray
    public StringArray sortById(StringArray array) throws InvalidArrayException {
        logger.debug("Sorting StringArray by ID using custom comparator: {}", array);

        StringArray[] sorted = Arrays.stream(new StringArray[]{array})
                .sorted(comparator.byId())
                .toArray(StringArray[]::new);

        logger.debug("Sorted by ID: {}", sorted[0]);
        return sorted[0];
    }

    public StringArray sortByLength(StringArray array) throws InvalidArrayException {
        logger.debug("Sorting StringArray by length using custom comparator: {}", array);

        StringArray[] sorted = Arrays.stream(new StringArray[]{array})
                .sorted(comparator.byLength())
                .toArray(StringArray[]::new);

        logger.debug("Sorted by length: {}", sorted[0]);
        return sorted[0];
    }

    public StringArray sortByFirstElement(StringArray array) throws InvalidArrayException {
        logger.debug("Sorting StringArray by first element using custom comparator: {}", array);

        StringArray[] sorted = Arrays.stream(new StringArray[]{array})
                .sorted(comparator.byFirstElement())
                .toArray(StringArray[]::new);

        logger.debug("Sorted by first element: {}", sorted[0]);
        return sorted[0];
    }

    public StringArray sortByLastElement(StringArray array) throws InvalidArrayException {
        logger.debug("Sorting StringArray by last element using custom comparator: {}", array);

        StringArray[] sorted = Arrays.stream(new StringArray[]{array})
                .sorted(comparator.byLastElement())
                .toArray(StringArray[]::new);

        logger.debug("Sorted by last element: {}", sorted[0]);
        return sorted[0];
    }

    public StringArray sortByAlphabeticalOrder(StringArray array) throws InvalidArrayException {
        logger.debug("Sorting StringArray by alphabetical order using custom comparator: {}", array);

        StringArray[] sorted = Arrays.stream(new StringArray[]{array})
                .sorted(comparator.byAlphabeticalOrder())
                .toArray(StringArray[]::new);

        logger.debug("Sorted by alphabetical order: {}", sorted[0]);
        return sorted[0];
    }

    public String[] findWordsLongerThan(StringArray array, int minLength) {
        logger.debug("Finding words longer than {} using Stream API in array: {}", minLength, array);

        String[] result = Arrays.stream(array.getArray())
                .filter(word -> word.length() > minLength)
                .toArray(String[]::new);

        logger.debug("Found {} words longer than {}", result.length, minLength);
        return result;
    }

    public String[] findWordsContaining(StringArray array, String substring) {
        logger.debug("Finding words containing '{}' using Stream API in array: {}", substring, array);

        String[] result = Arrays.stream(array.getArray())
                .filter(word -> word.toLowerCase().contains(substring.toLowerCase()))
                .toArray(String[]::new);

        logger.debug("Found {} words containing '{}'", result.length, substring);
        return result;
    }

    public String[] getUniqueWords(StringArray array) {
        logger.debug("Getting unique words using Stream API in array: {}", array);

        String[] result = Arrays.stream(array.getArray())
                .distinct()
                .toArray(String[]::new);

        logger.debug("Found {} unique words from {} total words", result.length, array.length());
        return result;
    }

    public String[] getWordsSortedByLength(StringArray array) {
        logger.debug("Getting words sorted by length using Stream API in array: {}", array);

        String[] result = Arrays.stream(array.getArray())
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byLength().compare(arr1, arr2);
                })
                .toArray(String[]::new);

        logger.debug("Words sorted by length successfully");
        return result;
    }

    public String[] getWordsSortedAlphabetically(StringArray array) {
        logger.debug("Getting words sorted alphabetically using Stream API in array: {}", array);

        String[] result = Arrays.stream(array.getArray())
                .sorted((s1, s2) -> {
                    StringArray arr1 = new StringArray(new String[]{s1});
                    StringArray arr2 = new StringArray(new String[]{s2});
                    return comparator.byFirstElement().compare(arr1, arr2);
                })
                .toArray(String[]::new);

        logger.debug("Words sorted alphabetically successfully");
        return result;
    }
}