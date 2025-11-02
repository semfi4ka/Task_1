package com.filippovich.arrayapp;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.parser.impl.ArrayParserImpl;
import com.filippovich.arrayapp.reader.impl.ArrayFileReaderImpl;
import com.filippovich.arrayapp.repository.impl.StringArrayRepositoryImpl;
import com.filippovich.arrayapp.comparator.impl.StringArrayComparatorImpl;
import com.filippovich.arrayapp.service.SortService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.filippovich.arrayapp.repository.*;
import com.filippovich.arrayapp.repository.specification.*;
import com.filippovich.arrayapp.warehouse.impl.Warehouse;


import com.filippovich.arrayapp.service.ArrayService;
import com.filippovich.arrayapp.service.impl.ArrayServiceImpl;
import com.filippovich.arrayapp.service.impl.SortServiceImpl;
import com.filippovich.arrayapp.service.impl.ArrayStreamService;
import com.filippovich.arrayapp.service.impl.SortStreamService;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    private static final Logger logger;

    static {
        initializeLogger();
        logger = LogManager.getLogger(App.class);
    }

    private static void initializeLogger() {
        try {
                File configFile = new File("data/log4j2.xml");
                ConfigurationSource source = new ConfigurationSource(new FileInputStream(configFile));
                Configurator.initialize(null, source);
                System.out.println("Log4j2 config loaded from: " + configFile.getPath());
                return;

        } catch (Exception e) {
            System.err.println("Failed to initialize Log4j2: " + e.getMessage());
            System.setProperty("log4j2.configurationFile", "");
        }
    }

    public static void main(String[] args) {
        logger.info("=== String Array Application Started ===");

        try {
            runAllTests();
            logger.info("All tests completed successfully!");

        } catch (Exception e) {
            logger.error("Application terminated with error", e);
        }
    }

    private static void runAllTests() throws InvalidArrayException {
        logger.info("Starting all test suites...");

        testStreamOperations();
        testStreamSorting();
        testSortingAlgorithms();
        testFileOperations();
        testRepositoryAndWarehouse();

        logger.info("All test suites completed!");
    }

    private static void testRepositoryAndWarehouse() throws InvalidArrayException {
        logger.info("=== REPOSITORY, WAREHOUSE & SPECIFICATION TEST ===");

        Warehouse warehouse = Warehouse.getInstance();
        StringArrayRepository repository = StringArrayRepositoryImpl.getInstance();
        repository.clear();
        logger.info("--- Test 1: Add to Repository & Check Warehouse (Observer) ---");

        StringArray array1 = ArrayFactory.createFromArray(new String[]{"apple", "banana", "cat"});
        logger.info("Created array 1: {}", array1);
        logger.info("Warehouse stats for array 1: {}", warehouse.getStatistics(array1.getId()).orElse(null));

        StringArray array2 = ArrayFactory.createFromArray(new String[]{"Zebra", "Lion", "Ant", "Tiger"});
        logger.info("Created array 2: {}", array2);
        logger.info("Warehouse stats for array 2: {}", warehouse.getStatistics(array2.getId()).orElse(null));

        StringArray array3 = ArrayFactory.createFromArray(new String[]{"a", "b"});
        logger.info("Created array 3: {}", array3);
        logger.info("Warehouse stats for array 3: {}", warehouse.getStatistics(array3.getId()).orElse(null));

        logger.info("--- Test 2: Remove from Repository & Check Warehouse ---");
        logger.info("Removing array 2...");
        repository.remove(array2);
        logger.info("Stats for removed array 2 in Warehouse: {}", warehouse.getStatistics(array2.getId()).orElse(null));
        logger.info("Total arrays in repository: {}", repository.getAll().size());

        logger.info("--- Test 3: Query Repository with Specifications ---");

        Specification idSpec = new IdSpecification(array1.getId());
        List<StringArray> idResult = repository.query(idSpec);
        logger.info("Query: Find by ID {}. Found: {}", array1.getId(), idResult);
        Specification maxLenSpec = new MaxLengthSpecification(1);
        List<StringArray> maxLenResult = repository.query(maxLenSpec);
        logger.info("Query: Find where max word length = 1. Found: {}", maxLenResult);

        logger.info("--- Test 4: Sort Repository results with Comparators ---");
        List<StringArray> allArrays = repository.getAll();
        logger.info("Unsorted list: {}", allArrays);

        StringArrayComparatorImpl comparatorFactory = new StringArrayComparatorImpl();

        allArrays.sort(comparatorFactory.byId());
        logger.info("Sorted by ID: {}", allArrays);

        allArrays.sort(comparatorFactory.byLength());
        logger.info("Sorted by Length (word count): {}", allArrays);

        allArrays.sort(comparatorFactory.byFirstElement());
        logger.info("Sorted by First Element: {}", allArrays);
    }


    private static void testFileOperations() {
        logger.info("=== FILE OPERATIONS TEST ===");
        ArrayFileReaderImpl fileReader = new ArrayFileReaderImpl();
        ArrayParserImpl parser = new ArrayParserImpl();

        try {
            logger.info("Reading and validating file...");
            List<String> validLines = fileReader.readValidLinesFromFile();

            logger.info("Generating file statistics...");
            fileReader.printFileStatistics();

            if (!validLines.isEmpty()) {
                List<StringArray> arrays = new ArrayList<>();
                for (String line : validLines) {
                    String[] words = parser.parseStringToArray(line);
                    StringArray array = ArrayFactory.createFromArray(words);
                    arrays.add(array);
                    logger.debug("Created array from line: {}", array);
                }
                processArraysFromFile(arrays);
            } else {
                logger.warn("No valid lines found in file!");
            }
        } catch (Exception e) {
            logger.error("File reading operation failed", e);
        }
    }

    private static void processArraysFromFile(java.util.List<StringArray> arrays) throws InvalidArrayException {
        logger.info("Processing arrays from file - Total arrays: {}", arrays.size());
        ArrayService arrayService = new ArrayServiceImpl();
        SortServiceImpl sortServiceImpl = new SortServiceImpl();

        for (int i = 0; i < arrays.size(); i++) {
            StringArray array = arrays.get(i);
            logger.info("--- Processing Word Array {} ---", i + 1);
            logger.info("Original array: {}", array);

            String shortest = arrayService.findShortestWord(array);
            String longest = arrayService.findLongestWord(array);
            double average = arrayService.calculateAverageLength(array);
            int totalChars = arrayService.calculateTotalCharacters(array);
            int wordsLongerThan5 = arrayService.countWordsLongerThan(array, 5);

            logger.info("Shortest word: '{}'", shortest);
            logger.info("Longest word: '{}'", longest);
            logger.info("Average word length: {}", average);
            logger.info("Total characters: {}", totalChars);
            logger.info("Words longer than 5 characters: {}", wordsLongerThan5);

            StringArray sorted = sortServiceImpl.sortByLengthBubble(array);
            logger.info("Sorted by length (Bubble): {}", sorted);

            logger.info("--- End of Array {} ---", i + 1);
        }
    }

    private static void testStreamOperations() throws InvalidArrayException {
        logger.info("=== STREAM API OPERATIONS TEST ===");

        String[] testData = {"apple", "banana", "cat", "elephant", "dog", "zebra"};
        StringArray array = ArrayFactory.createFromArray(testData);
        ArrayStreamService streamService = new ArrayStreamService();

        logger.info("Test array: {}", array);

        logger.info("--- Basic Stream Operations ---");
        logger.info("Shortest word: '{}'", streamService.findShortestWord(array));
        logger.info("Longest word: '{}'", streamService.findLongestWord(array));
        logger.info("Average length: {}", streamService.calculateAverageLength(array));
        logger.info("Total characters: {}", streamService.calculateTotalCharacters(array));
        logger.info("Words longer than 4: {}", streamService.countWordsLongerThan(array, 4));
        logger.info("Words shorter than 4: {}", streamService.countWordsShorterThan(array, 4));

        logger.info("--- Advanced Stream Operations ---");
        logger.info("First alphabetically: '{}'", streamService.findFirstAlphabetically(array));
        logger.info("Last alphabetically: '{}'", streamService.findLastAlphabetically(array));
        logger.info("Words starting with 'a': {}", streamService.countWordsStartingWith(array, 'a'));
        logger.info("Words ending with 'a': {}", streamService.countWordsEndingWith(array, 'a'));

        logger.info("--- Stream Transformations ---");
        logger.info("Words longer than 4: {}", Arrays.toString(streamService.findWordsLongerThan(array, 4)));
        logger.info("Words containing 'a': {}", Arrays.toString(streamService.findWordsContaining(array, "a")));
        logger.info("Unique words: {}", Arrays.toString(streamService.getUniqueWords(array)));
        logger.info("Sorted by length: {}", Arrays.toString(streamService.getWordsSortedByLength(array)));
        logger.info("Sorted alphabetically: {}", Arrays.toString(streamService.getWordsSortedAlphabetically(array)));

        logger.info("--- Stream Replacement ---");
        logger.info("Replace 'cat' with 'tiger': {}", streamService.replaceWords(array, "cat", "tiger"));
        logger.info("Replace words with length 3: {}", streamService.replaceWordsByLength(array, 3, "***"));
    }

    private static void testStreamSorting() throws InvalidArrayException {
        logger.info("=== STREAM SORTING TEST ===");

        String[] testData = {"elephant", "cat", "banana", "ant", "dog", "zebra", "cat"};
        StringArray array = ArrayFactory.createFromArray(testData);
        SortService sortService = new SortStreamService();

        logger.info("Original array: {}", array);

        logger.info("--- Basic Sorting Methods ---");
        logger.info("Bubble sort by length: {}", sortService.sortByLengthBubble(array));
        logger.info("Selection sort by length: {}", sortService.sortByLengthSelection(array));
        logger.info("Quick sort by length: {}", sortService.sortByLengthQuick(array));
        logger.info("Sorted by length descending: {}", sortService.sortByLengthDescending(array));
        logger.info("Sorted alphabetically: {}", sortService.sortAlphabetically(array));

        logger.info("--- Quick Sort Internal Methods Test ---");
        String[] testArray = array.getArray().clone();
        sortService.quickSortByLength(testArray, 0, testArray.length - 1);
        logger.info("Manual quick sort result: {}", Arrays.toString(testArray));

        // Тестирование partition
        String[] partitionTest = {"cat", "elephant", "ant", "dog"};
        int partitionIndex = sortService.partitionByLength(partitionTest, 0, partitionTest.length - 1);
        logger.info("Partition test - array: {}, pivot index: {}", Arrays.toString(partitionTest), partitionIndex);
    }

    private static void testSortingAlgorithms() throws InvalidArrayException {
        logger.info("=== SORTING ALGORITHMS TEST ===");

        String[] testData = {"elephant", "cat", "banana", "ant", "dog", "zebra", "programming"};
        StringArray array = ArrayFactory.createFromArray(testData);
        SortServiceImpl sortServiceImpl = new SortServiceImpl();

        logger.info("Original array: {}", array);

        logger.info("--- Length-Based Sorting ---");
        logger.info("Bubble sort (by length): {}", sortServiceImpl.sortByLengthBubble(array));
        logger.info("Selection sort (by length): {}", sortServiceImpl.sortByLengthSelection(array));
        logger.info("Quick sort (by length): {}", sortServiceImpl.sortByLengthQuick(array));
        logger.info("Length descending: {}", sortServiceImpl.sortByLengthDescending(array));

        logger.info("--- Alphabetical Sorting ---");
        logger.info("Alphabetical sort: {}", sortServiceImpl.sortAlphabetically(array));

        logger.info("--- Performance Comparison ---");
        long startTime, endTime;

        startTime = System.currentTimeMillis();
        StringArray bubbleSorted = sortServiceImpl.sortByLengthBubble(array);
        endTime = System.currentTimeMillis();
        logger.debug("Bubble sort completed in {} ms", endTime - startTime);

        startTime = System.currentTimeMillis();
        StringArray quickSorted = sortServiceImpl.sortByLengthQuick(array);
        endTime = System.currentTimeMillis();
        logger.debug("Quick sort completed in {} ms", endTime - startTime);

        logger.info("Sorting verification - Both produce same result: {}",
                Arrays.equals(bubbleSorted.getArray(), quickSorted.getArray()));
    }
}

