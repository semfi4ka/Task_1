package test.java.com.filippovich.arrayapp.service;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.service.impl.ArrayServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayServiceTest {

    private ArrayServiceImpl arrayService;
    private StringArray testArray;
    private StringArray emptyArray;
    private StringArray singleElementArray;

    @Before
    public void setUp() throws InvalidArrayException {
        arrayService = new ArrayServiceImpl();
        String[] words = {"apple", "banana", "cherry", "date", "elephant", "fig", "grape"};
        testArray = ArrayFactory.createFromArray(words);
        emptyArray = ArrayFactory.createFromArray(new String[0]);
        singleElementArray = ArrayFactory.createFromArray(new String[]{"single"});
    }

    @Test
    public void testFindShortestWord() {
        assertEquals("fig", arrayService.findShortestWord(testArray));
        assertEquals("", arrayService.findShortestWord(emptyArray));
        assertEquals("single", arrayService.findShortestWord(singleElementArray));
    }

    @Test
    public void testFindLongestWord() {
        assertEquals("elephant", arrayService.findLongestWord(testArray));
        assertEquals("", arrayService.findLongestWord(emptyArray));
        assertEquals("single", arrayService.findLongestWord(singleElementArray));
    }

    @Test
    public void testCalculateAverageLength() {
        double expectedAverage = 37.0 / 7.0;
        assertEquals(expectedAverage, arrayService.calculateAverageLength(testArray), 0.0001);

        assertEquals(0.0, arrayService.calculateAverageLength(emptyArray), 0.0);

        assertEquals(6.0, arrayService.calculateAverageLength(singleElementArray), 0.0);
    }

    @Test
    public void testCalculateTotalCharacters() {
        assertEquals(37, arrayService.calculateTotalCharacters(testArray));
        assertEquals(0, arrayService.calculateTotalCharacters(emptyArray));
        assertEquals(6, arrayService.calculateTotalCharacters(singleElementArray));
    }

    @Test
    public void testCountWordsLongerThan() {
        assertEquals(3, arrayService.countWordsLongerThan(testArray, 5));
        assertEquals(1, arrayService.countWordsLongerThan(testArray, 7));
        assertEquals(0, arrayService.countWordsLongerThan(testArray, 10));
        assertEquals(0, arrayService.countWordsLongerThan(emptyArray, 5));
        assertEquals(1, arrayService.countWordsLongerThan(singleElementArray, 5));
        assertEquals(0, arrayService.countWordsLongerThan(singleElementArray, 6));
    }

    @Test
    public void testCountWordsShorterThan() {
        assertEquals(2, arrayService.countWordsShorterThan(testArray, 5));
        assertEquals(1, arrayService.countWordsShorterThan(testArray, 4));
        assertEquals(0, arrayService.countWordsShorterThan(testArray, 3));
        assertEquals(0, arrayService.countWordsShorterThan(emptyArray, 5));
        assertEquals(0, arrayService.countWordsShorterThan(singleElementArray, 6));
        assertEquals(1, arrayService.countWordsShorterThan(singleElementArray, 7));
    }

    @Test
    public void testReplaceWords() throws InvalidArrayException {
        StringArray result = arrayService.replaceWords(testArray, "banana", "orange");
        assertArrayEquals(new String[]{"apple", "orange", "cherry", "date", "elephant", "fig", "grape"},
                result.getArray());
        result = arrayService.replaceWords(testArray, "nonexistent", "orange");
        assertArrayEquals(testArray.getArray(), result.getArray());

        result = arrayService.replaceWords(emptyArray, "word", "newWord");
        assertArrayEquals(new String[0], result.getArray());

        result = arrayService.replaceWords(singleElementArray, "single", "replaced");
        assertArrayEquals(new String[]{"replaced"}, result.getArray());
    }

    @Test
    public void testReplaceWordsByLength() throws InvalidArrayException {
        StringArray result = arrayService.replaceWordsByLength(testArray, 5, "FIVE");
        assertArrayEquals(new String[]{"FIVE", "banana", "cherry", "date", "elephant", "fig", "FIVE"},
                result.getArray());

        result = arrayService.replaceWordsByLength(testArray, 10, "TEN");
        assertArrayEquals(testArray.getArray(), result.getArray());

        result = arrayService.replaceWordsByLength(emptyArray, 5, "FIVE");
        assertArrayEquals(new String[0], result.getArray());

        result = arrayService.replaceWordsByLength(singleElementArray, 6, "SIX");
        assertArrayEquals(new String[]{"SIX"}, result.getArray());
    }

    @Test
    public void testFindFirstAlphabetically() {
        assertEquals("apple", arrayService.findFirstAlphabetically(testArray));

        assertEquals("", arrayService.findFirstAlphabetically(emptyArray));

        assertEquals("single", arrayService.findFirstAlphabetically(singleElementArray));

        StringArray mixedCaseArray;
        try {
            mixedCaseArray = ArrayFactory.createFromArray(new String[]{"Zebra", "apple", "Banana"});
            assertEquals("apple", arrayService.findFirstAlphabetically(mixedCaseArray));
        } catch (InvalidArrayException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testFindLastAlphabetically() {
        assertEquals("grape", arrayService.findLastAlphabetically(testArray));

        assertEquals("", arrayService.findLastAlphabetically(emptyArray));

        assertEquals("single", arrayService.findLastAlphabetically(singleElementArray));

        StringArray mixedCaseArray;
        try {
            mixedCaseArray = ArrayFactory.createFromArray(new String[]{"zebra", "Apple", "banana"});
            assertEquals("zebra", arrayService.findLastAlphabetically(mixedCaseArray));
        } catch (InvalidArrayException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testCountWordsStartingWith() {
        assertEquals(1, arrayService.countWordsStartingWith(testArray, 'a'));
        assertEquals(1, arrayService.countWordsStartingWith(testArray, 'b'));
        assertEquals(1, arrayService.countWordsStartingWith(testArray, 'c'));
        assertEquals(0, arrayService.countWordsStartingWith(testArray, 'x'));

        assertEquals(1, arrayService.countWordsStartingWith(testArray, 'A'));
        assertEquals(1, arrayService.countWordsStartingWith(testArray, 'B'));

        assertEquals(0, arrayService.countWordsStartingWith(emptyArray, 'a'));

        assertEquals(1, arrayService.countWordsStartingWith(singleElementArray, 's'));
        assertEquals(0, arrayService.countWordsStartingWith(singleElementArray, 'x'));
    }

    @Test
    public void testCountWordsEndingWith() {
        assertEquals(3, arrayService.countWordsEndingWith(testArray, 'e'));
        assertEquals(1, arrayService.countWordsEndingWith(testArray, 'a'));
        assertEquals(0, arrayService.countWordsEndingWith(testArray, 'x'));

        assertEquals(3, arrayService.countWordsEndingWith(testArray, 'E'));
        assertEquals(1, arrayService.countWordsEndingWith(testArray, 'A'));

        assertEquals(0, arrayService.countWordsEndingWith(emptyArray, 'a'));

        assertEquals(1, arrayService.countWordsEndingWith(singleElementArray, 'e'));
        assertEquals(0, arrayService.countWordsEndingWith(singleElementArray, 'x'));
    }

    @Test
    public void testEdgeCases() throws InvalidArrayException {
        StringArray arrayWithEmptyStrings = ArrayFactory.createFromArray(new String[]{"", "word", ""});

        assertEquals("", arrayService.findShortestWord(arrayWithEmptyStrings));
        assertEquals("word", arrayService.findLongestWord(arrayWithEmptyStrings));
        assertEquals(4.0 / 3.0, arrayService.calculateAverageLength(arrayWithEmptyStrings), 0.0001);
        assertEquals(4, arrayService.calculateTotalCharacters(arrayWithEmptyStrings));
        assertEquals(1, arrayService.countWordsStartingWith(arrayWithEmptyStrings, 'w'));
        assertEquals(0, arrayService.countWordsStartingWith(arrayWithEmptyStrings, 'a'));
        assertEquals(1, arrayService.countWordsEndingWith(arrayWithEmptyStrings, 'd'));
    }

    @Test
    public void testArrayWithDuplicateWords() throws InvalidArrayException {
        StringArray duplicateArray = ArrayFactory.createFromArray(new String[]{"apple", "apple", "banana", "apple"});

        assertEquals("apple", arrayService.findShortestWord(duplicateArray));
        assertEquals("banana", arrayService.findLongestWord(duplicateArray));
        assertEquals(3, arrayService.countWordsStartingWith(duplicateArray, 'a'));
        assertEquals(1, arrayService.countWordsStartingWith(duplicateArray, 'b'));

        StringArray replaced = arrayService.replaceWords(duplicateArray, "apple", "orange");
        assertArrayEquals(new String[]{"orange", "orange", "banana", "orange"}, replaced.getArray());
    }

    @Test
    public void testArrayWithSpecialCharacters() throws InvalidArrayException {
        StringArray specialArray = ArrayFactory.createFromArray(new String[]{"word!", "test123", "hello-world"});

        assertEquals("word!", arrayService.findShortestWord(specialArray));
        assertEquals("hello-world", arrayService.findLongestWord(specialArray));
        assertEquals(1, arrayService.countWordsStartingWith(specialArray, 'w'));
        assertEquals(1, arrayService.countWordsEndingWith(specialArray, '!'));
        assertEquals(1, arrayService.countWordsEndingWith(specialArray, '3'));
        assertEquals(1, arrayService.countWordsEndingWith(specialArray, 'd'));
    }

    @Test
    public void testPerformanceWithLargeArray() throws InvalidArrayException {
        String[] largeArray = new String[1000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = "word" + i;
        }
        StringArray largeStringArray = ArrayFactory.createFromArray(largeArray);

        assertEquals("word0", arrayService.findFirstAlphabetically(largeStringArray));
        assertEquals("word999", arrayService.findLastAlphabetically(largeStringArray));
        assertEquals(1000, arrayService.countWordsStartingWith(largeStringArray, 'w'));
    }
}