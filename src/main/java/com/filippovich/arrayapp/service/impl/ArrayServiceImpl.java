package com.filippovich.arrayapp.service.impl;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.service.ArrayService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrayServiceImpl implements ArrayService {
    private static final Logger logger = LogManager.getLogger(ArrayServiceImpl.class);

    @Override
    public String findShortestWord(StringArray array) {
        logger.debug("Finding shortest word in array: {}", array);

        if (array.isEmpty()) {
            logger.warn("Attempt to find shortest word in empty array");
            return "";
        }

        String[] arr = array.getArray();
        String shortest = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].length() < shortest.length()) {
                shortest = arr[i];
            }
        }

        logger.debug("Shortest word found: '{}' (length: {})", shortest, shortest.length());
        return shortest;
    }

    @Override
    public String findLongestWord(StringArray array) {
        logger.debug("Finding longest word in array: {}", array);

        if (array.isEmpty()) {
            logger.warn("Attempt to find longest word in empty array");
            return "";
        }

        String[] arr = array.getArray();
        String longest = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].length() > longest.length()) {
                longest = arr[i];
            }
        }

        logger.debug("Longest word found: '{}' (length: {})", longest, longest.length());
        return longest;
    }

    @Override
    public double calculateAverageLength(StringArray array) {
        logger.debug("Calculating average word length in array: {}", array);

        if (array.isEmpty()) {
            logger.warn("Attempt to calculate average length of empty array");
            return 0.0;
        }

        String[] arr = array.getArray();
        int totalLength = 0;
        for (String word : arr) {
            totalLength += word.length();
        }

        double average = (double) totalLength / array.length();
        logger.debug("Average word length: {}", average);
        return average;
    }

    @Override
    public int calculateTotalCharacters(StringArray array) {
        logger.debug("Calculating total characters in array: {}", array);

        String[] arr = array.getArray();
        int total = 0;
        for (String word : arr) {
            total += word.length();
        }

        logger.debug("Total characters: {}", total);
        return total;
    }

    @Override
    public int countWordsLongerThan(StringArray array, int minLength) {
        logger.debug("Counting words longer than {} in array: {}", minLength, array);

        String[] arr = array.getArray();
        int count = 0;
        for (String word : arr) {
            if (word.length() > minLength) {
                count++;
            }
        }

        logger.debug("Words longer than {}: {}", minLength, count);
        return count;
    }

    @Override
    public int countWordsShorterThan(StringArray array, int maxLength) {
        logger.debug("Counting words shorter than {} in array: {}", maxLength, array);

        String[] arr = array.getArray();
        int count = 0;
        for (String word : arr) {
            if (word.length() < maxLength) {
                count++;
            }
        }

        logger.debug("Words shorter than {}: {}", maxLength, count);
        return count;
    }

    @Override
    public StringArray replaceWords(StringArray array, String oldWord, String newWord) throws InvalidArrayException {
        logger.debug("Replacing words in array: {}, oldWord: '{}', newWord: '{}'", array, oldWord, newWord);

        String[] arr = array.getArray();
        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i].equals(oldWord) ? newWord : arr[i];
        }

        StringArray replacedArray = ArrayFactory.createFromArray(result);
        logger.debug("Words replaced. Result: {}", replacedArray);
        return replacedArray;
    }

    @Override
    public StringArray replaceWordsByLength(StringArray array, int targetLength, String newWord) throws InvalidArrayException {
        logger.debug("Replacing words with length {} with '{}' in array: {}", targetLength, newWord, array);

        String[] arr = array.getArray();
        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = (arr[i].length() == targetLength) ? newWord : arr[i];
        }

        StringArray replacedArray = ArrayFactory.createFromArray(result);
        logger.debug("Words replaced by length. Result: {}", replacedArray);
        return replacedArray;
    }

    @Override
    public String findFirstAlphabetically(StringArray array) {
        logger.debug("Finding first word alphabetically in array: {}", array);

        if (array.isEmpty()) {
            logger.warn("Attempt to find first word in empty array");
            return "";
        }

        String[] arr = array.getArray();
        String first = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].compareToIgnoreCase(first) < 0) {
                first = arr[i];
            }
        }

        logger.debug("First word alphabetically: '{}'", first);
        return first;
    }

    @Override
    public String findLastAlphabetically(StringArray array) {
        logger.debug("Finding last word alphabetically in array: {}", array);

        if (array.isEmpty()) {
            logger.warn("Attempt to find last word in empty array");
            return "";
        }

        String[] arr = array.getArray();
        String last = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].compareToIgnoreCase(last) > 0) {
                last = arr[i];
            }
        }

        logger.debug("Last word alphabetically: '{}'", last);
        return last;
    }

    @Override
    public int countWordsStartingWith(StringArray array, char letter) {
        logger.debug("Counting words starting with '{}' in array: {}", letter, array);

        String[] arr = array.getArray();
        int count = 0;
        for (String word : arr) {
            if (!word.isEmpty() && Character.toLowerCase(word.charAt(0)) == Character.toLowerCase(letter)) {
                count++;
            }
        }

        logger.debug("Words starting with '{}': {}", letter, count);
        return count;
    }

    @Override
    public int countWordsEndingWith(StringArray array, char letter) {
        logger.debug("Counting words ending with '{}' in array: {}", letter, array);

        String[] arr = array.getArray();
        int count = 0;
        for (String word : arr) {
            if (!word.isEmpty() &&
                    Character.toLowerCase(word.charAt(word.length() - 1)) == Character.toLowerCase(letter)) {
                count++;
            }
        }

        logger.debug("Words ending with '{}': {}", letter, count);
        return count;
    }
}