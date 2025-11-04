package test.java.com.filippovich.arrayapp.service;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.service.impl.SortStreamService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SortStreamServiceTest {

    private SortStreamService sortService;
    private StringArray testArray;
    private StringArray emptyArray;
    private StringArray singleElementArray;
    private StringArray sortedArray;
    private StringArray reverseSortedArray;

    @Before
    public void setUp() throws InvalidArrayException {
        sortService = new SortStreamService();

        testArray = ArrayFactory.createFromArray(new String[]{
                "elephant", "cat", "banana", "a", "dog", "apple"
        });

        emptyArray = ArrayFactory.createFromArray(new String[0]);
        singleElementArray = ArrayFactory.createFromArray(new String[]{"single"});

        sortedArray = ArrayFactory.createFromArray(new String[]{
                "a", "cat", "dog", "apple", "banana", "elephant"
        });

        reverseSortedArray = ArrayFactory.createFromArray(new String[]{
                "elephant", "banana", "apple", "cat", "dog", "a"
        });
    }

    private boolean containsAllElements(StringArray result, String[] expectedElements) {
        String[] resultArray = result.getArray();
        Set<String> resultSet = new HashSet<>(Arrays.asList(resultArray));
        Set<String> expectedSet = new HashSet<>(Arrays.asList(expectedElements));
        return resultSet.equals(expectedSet);
    }

    private boolean containsElement(StringArray array, String element) {
        for (String item : array.getArray()) {
            if (item.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testSortByLengthBubble() throws InvalidArrayException {
        StringArray result = sortService.sortByLengthBubble(testArray);
        assertArrayEquals(new String[]{"a", "cat", "dog", "apple", "banana", "elephant"},
                result.getArray());

        result = sortService.sortByLengthBubble(emptyArray);
        assertArrayEquals(new String[0], result.getArray());

        result = sortService.sortByLengthBubble(singleElementArray);
        assertArrayEquals(new String[]{"single"}, result.getArray());

        result = sortService.sortByLengthBubble(sortedArray);
        assertArrayEquals(sortedArray.getArray(), result.getArray());

        StringArray sameLengthArray = ArrayFactory.createFromArray(new String[]{"dog", "cat", "bat"});
        result = sortService.sortByLengthBubble(sameLengthArray);
        assertEquals(3, result.length());
        assertTrue(containsAllElements(result, new String[]{"dog", "cat", "bat"}));
    }

    @Test
    public void testSortByLengthSelection() throws InvalidArrayException {
        StringArray result = sortService.sortByLengthSelection(testArray);
        assertArrayEquals(new String[]{"a", "cat", "dog", "apple", "banana", "elephant"},
                result.getArray());

        result = sortService.sortByLengthSelection(emptyArray);
        assertArrayEquals(new String[0], result.getArray());

        result = sortService.sortByLengthSelection(singleElementArray);
        assertArrayEquals(new String[]{"single"}, result.getArray());

        result = sortService.sortByLengthSelection(sortedArray);
        assertArrayEquals(sortedArray.getArray(), result.getArray());
    }

    @Test
    public void testSortByLengthQuick() throws InvalidArrayException {
        StringArray result = sortService.sortByLengthQuick(testArray);
        assertArrayEquals(new String[]{"a", "cat", "dog", "apple", "banana", "elephant"},
                result.getArray());

        result = sortService.sortByLengthQuick(emptyArray);
        assertArrayEquals(new String[0], result.getArray());

        result = sortService.sortByLengthQuick(singleElementArray);
        assertArrayEquals(new String[]{"single"}, result.getArray());

        result = sortService.sortByLengthQuick(sortedArray);
        assertArrayEquals(sortedArray.getArray(), result.getArray());

        result = sortService.sortByLengthQuick(reverseSortedArray);
        assertArrayEquals(sortedArray.getArray(), result.getArray());
    }

    @Test
    public void testQuickSortByLengthMethod()  {
        String[] arr = {"elephant", "cat", "banana", "a", "dog", "apple"};
        sortService.quickSortByLength(arr, 0, arr.length - 1);
        assertArrayEquals(new String[]{"a", "cat", "dog", "apple", "banana", "elephant"}, arr);

        String[] singleArr = {"single"};
        sortService.quickSortByLength(singleArr, 0, singleArr.length - 1);
        assertArrayEquals(new String[]{"single"}, singleArr);

        String[] twoArr = {"longer", "short"};
        sortService.quickSortByLength(twoArr, 0, twoArr.length - 1);
        assertArrayEquals(new String[]{"short", "longer"}, twoArr);
    }

    @Test
    public void testPartitionByLengthMethod() {
        String[] arr = {"elephant", "cat", "banana", "a", "dog", "apple"};
        int pivotIndex = sortService.partitionByLength(arr, 0, arr.length - 1);

        int pivotLength = arr[pivotIndex].length();
        for (int i = 0; i < pivotIndex; i++) {
            assertTrue(arr[i].length() <= pivotLength);
        }

        for (int i = pivotIndex + 1; i < arr.length; i++) {
            assertTrue(arr[i].length() >= pivotLength);
        }
    }

    @Test
    public void testSortAlphabetically() throws InvalidArrayException {
        StringArray result = sortService.sortAlphabetically(testArray);
        assertArrayEquals(new String[]{"a", "apple", "banana", "cat", "dog", "elephant"},
                result.getArray());

        result = sortService.sortAlphabetically(emptyArray);
        assertArrayEquals(new String[0], result.getArray());

        result = sortService.sortAlphabetically(singleElementArray);
        assertArrayEquals(new String[]{"single"}, result.getArray());

        StringArray mixedCaseArray = ArrayFactory.createFromArray(new String[]{
                "Zebra", "apple", "Banana", "cat"
        });
        result = sortService.sortAlphabetically(mixedCaseArray);
        assertArrayEquals(new String[]{"apple", "Banana", "cat", "Zebra"},
                result.getArray());

        StringArray specialArray = ArrayFactory.createFromArray(new String[]{
                "word!", "test", "hello", "123number"
        });
        result = sortService.sortAlphabetically(specialArray);
        assertArrayEquals(new String[]{"123number", "hello", "test", "word!"},
                result.getArray());
    }

    @Test
    public void testSortByLengthDescending() throws InvalidArrayException {
        StringArray result = sortService.sortByLengthDescending(testArray);
        assertArrayEquals(new String[]{"elephant", "banana", "apple", "cat", "dog", "a"},
                result.getArray());

        result = sortService.sortByLengthDescending(emptyArray);
        assertArrayEquals(new String[0], result.getArray());

        result = sortService.sortByLengthDescending(singleElementArray);
        assertArrayEquals(new String[]{"single"}, result.getArray());

        result = sortService.sortByLengthDescending(reverseSortedArray);
        assertArrayEquals(reverseSortedArray.getArray(), result.getArray());
    }

    @Test
    public void testAllSortMethodsProduceSameResult() throws InvalidArrayException {
        StringArray bubbleResult = sortService.sortByLengthBubble(testArray);
        StringArray selectionResult = sortService.sortByLengthSelection(testArray);
        StringArray quickResult = sortService.sortByLengthQuick(testArray);

        assertArrayEquals(bubbleResult.getArray(), selectionResult.getArray());
        assertArrayEquals(selectionResult.getArray(), quickResult.getArray());
    }

    @Test
    public void testSortWithDuplicateLengths() throws InvalidArrayException {
        StringArray duplicateLengthArray = ArrayFactory.createFromArray(new String[]{
                "dog", "cat", "bat", "elephant", "banana", "orange"
        });

        StringArray result = sortService.sortByLengthBubble(duplicateLengthArray);

        int[] expectedLengths = {3, 3, 3, 6, 6, 8};
        for (int i = 0; i < result.length(); i++) {
            assertEquals(expectedLengths[i], result.getArray()[i].length());
        }

        String[] original = duplicateLengthArray.getArray();
        for (String word : original) {
            assertTrue("Array should contain: " + word, containsElement(result, word));
        }
    }

    @Test
    public void testSortPerformance() throws InvalidArrayException {
        String[] largeArray = new String[100];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = "word" + (99 - i);
        }
        StringArray largeStringArray = ArrayFactory.createFromArray(largeArray);

        StringArray bubbleResult = sortService.sortByLengthBubble(largeStringArray);
        StringArray selectionResult = sortService.sortByLengthSelection(largeStringArray);
        StringArray quickResult = sortService.sortByLengthQuick(largeStringArray);

        assertEquals(largeArray.length, bubbleResult.length());
        assertEquals(largeArray.length, selectionResult.length());
        assertEquals(largeArray.length, quickResult.length());

        String[] bubbleArr = bubbleResult.getArray();
        for (int i = 0; i < bubbleArr.length - 1; i++) {
            assertTrue(bubbleArr[i].length() <= bubbleArr[i + 1].length());
        }
    }

    @Test
    public void testSortStabilityCheck() throws InvalidArrayException {
        StringArray stableTestArray = ArrayFactory.createFromArray(new String[]{
                "cat", "dog", "bat"
        });

        StringArray result = sortService.sortByLengthBubble(stableTestArray);

        assertEquals(3, result.length());
        assertTrue(containsAllElements(result, new String[]{"cat", "dog", "bat"}));
    }

    @Test
    public void testEdgeCases() throws InvalidArrayException {

        StringArray arrayWithEmpty = ArrayFactory.createFromArray(new String[]{
                "hello", "", "world", "a"
        });

        StringArray result = sortService.sortByLengthBubble(arrayWithEmpty);
        assertEquals("", result.getArray()[0]);
        assertEquals("a", result.getArray()[1]);
        assertEquals("hello", result.getArray()[2]);
        assertEquals("world", result.getArray()[3]);
    }

    @Test
    public void testSortWithVeryLongWords() throws InvalidArrayException {
        String veryLongWord = "a".repeat(1000);
        StringArray longWordsArray = ArrayFactory.createFromArray(new String[]{
                "short", veryLongWord, "mediumlength", "a"
        });

        StringArray result = sortService.sortByLengthQuick(longWordsArray);

        assertEquals("a", result.getArray()[0]);
        assertEquals("short", result.getArray()[1]);
        assertEquals("mediumlength", result.getArray()[2]);
        assertEquals(veryLongWord, result.getArray()[3]);
    }

    @Test
    public void testSortWithSpecialCharactersAndNumbers() throws InvalidArrayException {
        StringArray mixedArray = ArrayFactory.createFromArray(new String[]{
                "123", "!@#", "abc", "ABC", "test1", "test2"
        });

        StringArray result = sortService.sortAlphabetically(mixedArray);
        assertArrayEquals(new String[]{"!@#", "123", "abc", "ABC", "test1", "test2"},
                result.getArray());
    }

    @Test
    public void testSortEmptyStrings() throws InvalidArrayException {
        StringArray emptyStringsArray = ArrayFactory.createFromArray(new String[]{
                "", "", ""
        });

        StringArray result = sortService.sortByLengthBubble(emptyStringsArray);
        assertEquals(3, result.length());
        for (int i = 0; i < result.length(); i++) {
            assertEquals("", result.getArray()[i]);
        }
    }

    @Test
    public void testSortSingleCharacterWords() throws InvalidArrayException {
        StringArray singleCharArray = ArrayFactory.createFromArray(new String[]{
                "z", "a", "m", "b", "c"
        });

        StringArray result = sortService.sortAlphabetically(singleCharArray);
        assertArrayEquals(new String[]{"a", "b", "c", "m", "z"}, result.getArray());
    }
}