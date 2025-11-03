package test.java.com.filippovich.arrayapp.comporator;

import com.filippovich.arrayapp.comparator.impl.StringArrayComparatorImpl;
import com.filippovich.arrayapp.entity.StringArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class StringArrayComparatorTest {

    private StringArrayComparatorImpl comparator;
    private StringArray array1;
    private StringArray array2;
    private StringArray array3;
    private StringArray emptyArray;

    @BeforeEach
    void setUp() {
        comparator = new StringArrayComparatorImpl();

        array1 = new StringArray(new String[]{"apple", "banana", "cherry"});
        array2 = new StringArray(new String[]{"date", "elderberry"});
        array3 = new StringArray(new String[]{"apple", "banana", "date"});
        emptyArray = new StringArray(new String[]{});
    }

    @Test
    void testById() {
        Comparator<StringArray> idComparator = comparator.byId();

        assertEquals(0, idComparator.compare(array1, array1));
        assertEquals(0, idComparator.compare(array2, array2));

        int result1 = idComparator.compare(array1, array2);
        int result2 = idComparator.compare(array2, array1);
        assertTrue((result1 < 0 && result2 > 0) || (result1 > 0 && result2 < 0));
    }

    @Test
    void testByLength() {
        Comparator<StringArray> lengthComparator = comparator.byLength();

        assertTrue(lengthComparator.compare(array1, array2) > 0); // 3 > 2
        assertTrue(lengthComparator.compare(array2, array3) < 0); // 2 < 3
        assertEquals(0, lengthComparator.compare(array1, array3)); // 3 == 3
        assertTrue(lengthComparator.compare(emptyArray, array1) < 0); // 0 < 3
        assertEquals(0, lengthComparator.compare(array1, array1));
    }

    @Test
    void testByFirstElement() {
        Comparator<StringArray> firstElementComparator = comparator.byFirstElement();

        assertTrue(firstElementComparator.compare(array1, array2) < 0); // "apple" < "date"
        assertTrue(firstElementComparator.compare(array2, array1) > 0); // "date" > "apple"
        assertEquals(0, firstElementComparator.compare(array1, array1));

        assertTrue(firstElementComparator.compare(emptyArray, array1) < 0);
        assertTrue(firstElementComparator.compare(array1, emptyArray) > 0);
        assertEquals(0, firstElementComparator.compare(emptyArray, emptyArray));
    }

    @Test
    void testByLastElement() {
        Comparator<StringArray> lastElementComparator = comparator.byLastElement();

        assertTrue(lastElementComparator.compare(array1, array2) < 0); // "cherry" < "elderberry"
        assertTrue(lastElementComparator.compare(array2, array1) > 0); // "elderberry" > "cherry"
        assertTrue(lastElementComparator.compare(array1, array3) < 0); // "cherry" < "date"
        assertEquals(0, lastElementComparator.compare(array1, array1));

        assertTrue(lastElementComparator.compare(emptyArray, array1) < 0);
        assertTrue(lastElementComparator.compare(array1, emptyArray) > 0);
        assertEquals(0, lastElementComparator.compare(emptyArray, emptyArray));
    }

    @Test
    void testByAlphabeticalOrder() {
        Comparator<StringArray> alphabeticalComparator = comparator.byAlphabeticalOrder();
        StringArray arrayA = new StringArray(new String[]{"apple", "banana", "cherry"});
        StringArray arrayB = new StringArray(new String[]{"apple", "banana", "date"});

        assertTrue(alphabeticalComparator.compare(arrayA, arrayB) < 0); // "cherry" < "date"
        assertTrue(alphabeticalComparator.compare(arrayB, arrayA) > 0); // "date" > "cherry"

        assertTrue(alphabeticalComparator.compare(array1, array2) < 0); // "apple" < "date"

        StringArray shortArray = new StringArray(new String[]{"apple", "banana"});
        StringArray longArray = new StringArray(new String[]{"apple", "banana", "cherry"});

        assertTrue(alphabeticalComparator.compare(shortArray, longArray) < 0); // shorter comes first
        assertTrue(alphabeticalComparator.compare(longArray, shortArray) > 0); // longer comes after

        StringArray lowerCase = new StringArray(new String[]{"APPLE", "BANANA"});
        StringArray upperCase = new StringArray(new String[]{"apple", "banana"});

        assertEquals(0, alphabeticalComparator.compare(lowerCase, upperCase));

        assertTrue(alphabeticalComparator.compare(emptyArray, array1) < 0);
        assertTrue(alphabeticalComparator.compare(array1, emptyArray) > 0);
        assertEquals(0, alphabeticalComparator.compare(emptyArray, emptyArray));
    }

    @Test
    void testByAlphabeticalOrderWithDifferentLengths() {
        Comparator<StringArray> alphabeticalComparator = comparator.byAlphabeticalOrder();

        StringArray prefixArray = new StringArray(new String[]{"a", "b"});
        StringArray longerArray = new StringArray(new String[]{"a", "b", "c"});

        assertTrue(alphabeticalComparator.compare(prefixArray, longerArray) < 0);
        assertTrue(alphabeticalComparator.compare(longerArray, prefixArray) > 0);

        StringArray arrayX = new StringArray(new String[]{"x", "y", "z"});
        StringArray arrayM = new StringArray(new String[]{"m", "n", "o"});

        assertTrue(alphabeticalComparator.compare(arrayM, arrayX) < 0);
        assertTrue(alphabeticalComparator.compare(arrayX, arrayM) > 0);
    }

    @Test
    void testComparatorChaining() {
        Comparator<StringArray> chainedComparator = comparator.byLength()
                .thenComparing(comparator.byFirstElement());

        StringArray arrayA = new StringArray(new String[]{"apple"});
        StringArray arrayB = new StringArray(new String[]{"banana"});
        StringArray arrayC = new StringArray(new String[]{"apple", "cherry"});

        assertTrue(chainedComparator.compare(arrayA, arrayB) < 0);

        assertTrue(chainedComparator.compare(arrayA, arrayC) < 0);
    }

    @Test
    void testArraysWithNullElements() {
        StringArray withNull = new StringArray(new String[]{"a", null, "c"});
        StringArray withoutNull = new StringArray(new String[]{"a", "b", "c"});
        Comparator<StringArray> alphabeticalComparator = comparator.byAlphabeticalOrder();
        assertThrows(NullPointerException.class,
                () -> alphabeticalComparator.compare(withNull, withoutNull));
    }

    @Test
    void testSingleElementArrays() {
        StringArray single1 = new StringArray(new String[]{"a"});
        StringArray single2 = new StringArray(new String[]{"b"});

        Comparator<StringArray> firstElementComparator = comparator.byFirstElement();
        Comparator<StringArray> lastElementComparator = comparator.byLastElement();

        assertEquals(firstElementComparator.compare(single1, single2),
                lastElementComparator.compare(single1, single2));
    }

    @Test
    void testEmptyArraysComparison() {
        Comparator<StringArray> lengthComparator = comparator.byLength();
        Comparator<StringArray> firstElementComparator = comparator.byFirstElement();
        Comparator<StringArray> lastElementComparator = comparator.byLastElement();
        Comparator<StringArray> alphabeticalComparator = comparator.byAlphabeticalOrder();

        StringArray anotherEmptyArray = new StringArray(new String[]{});

        assertEquals(0, lengthComparator.compare(emptyArray, anotherEmptyArray));
        assertEquals(0, firstElementComparator.compare(emptyArray, anotherEmptyArray));
        assertEquals(0, lastElementComparator.compare(emptyArray, anotherEmptyArray));
        assertEquals(0, alphabeticalComparator.compare(emptyArray, anotherEmptyArray));
    }
}