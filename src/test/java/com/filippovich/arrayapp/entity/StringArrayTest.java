package test.java.com.filippovich.arrayapp.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.filippovich.arrayapp.entity.StringArray;

import java.util.UUID;

public class StringArrayTest {

    private StringArray emptyArray;
    private StringArray stringArray;
    private String[] testData;

    @BeforeEach
    void setUp() {
        testData = new String[]{"apple", "banana", "cherry"};
        emptyArray = new StringArray(new String[0]);
        stringArray = new StringArray(testData);
    }

    @Test
    void testConstructorWithNull() {
        StringArray array = new StringArray(null);
        assertNotNull(array);
        assertEquals(0, array.length());
        assertTrue(array.isEmpty());
    }

    @Test
    void testConstructorWithEmptyArray() {
        StringArray array = new StringArray(new String[0]);
        assertNotNull(array);
        assertEquals(0, array.length());
        assertTrue(array.isEmpty());
    }

    @Test
    void testConstructorWithValidArray() {
        assertNotNull(stringArray);
        assertEquals(3, stringArray.length());
        assertFalse(stringArray.isEmpty());
    }

    @Test
    void testGetId() {
        UUID id = stringArray.getId();
        assertNotNull(id);

        StringArray anotherArray = new StringArray(testData);
        assertNotEquals(id, anotherArray.getId());
    }

    @Test
    void testGetArray() {
        String[] returnedArray = stringArray.getArray();
        assertNotNull(returnedArray);
        assertEquals(3, returnedArray.length);
        assertArrayEquals(testData, returnedArray);

        assertNotSame(testData, returnedArray);

        returnedArray[0] = "modified";
        assertEquals("apple", stringArray.getArray()[0]);
    }

    @Test
    void testLength() {
        assertEquals(3, stringArray.length());
        assertEquals(0, emptyArray.length());

        StringArray singleElementArray = new StringArray(new String[]{"single"});
        assertEquals(1, singleElementArray.length());
    }

    @Test
    void testIsEmpty() {
        assertTrue(emptyArray.isEmpty());
        assertFalse(stringArray.isEmpty());

        StringArray nullConstructorArray = new StringArray(null);
        assertTrue(nullConstructorArray.isEmpty());
    }

    @Test
    void testToString() {
        String result = stringArray.toString();
        assertNotNull(result);
        assertTrue(result.contains("StringArrayImpl"));
        assertTrue(result.contains("id="));
        assertTrue(result.contains("array=["));
        assertTrue(result.contains("apple"));
        assertTrue(result.contains("banana"));
        assertTrue(result.contains("cherry"));
        String emptyResult = emptyArray.toString();
        assertTrue(emptyResult.contains("array=[]"));
    }

    @Test
    void testToStringWithNullElements() {
        String[] arrayWithNulls = new String[]{"first", null, "third"};
        StringArray array = new StringArray(arrayWithNulls);
        String result = array.toString();
        assertNotNull(result);
        assertTrue(result.contains("first"));
        assertTrue(result.contains("third"));
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(stringArray.equals(stringArray));
    }

    @Test
    void testEqualsNull() {
        assertFalse(stringArray.equals(null));
    }

    @Test
    void testEqualsDifferentClass() {
        assertFalse(stringArray.equals("not a StringArray"));
    }

    @Test
    void testEqualsDifferentId() {
        StringArray anotherArray = new StringArray(testData);
        assertFalse(stringArray.equals(anotherArray));
    }

    @Test
    void testEqualsSameIdDifferentContent() {
        String[] differentData = new String[]{"different", "data"};
        StringArray array1 = new StringArray(testData);
        StringArray array2 = new StringArray(differentData);
        assertFalse(array1.equals(array2));
    }

    @Test
    void testEqualsSameContentDifferentInstances() {
        StringArray array1 = new StringArray(testData);
        StringArray array2 = new StringArray(testData);
        assertFalse(array1.equals(array2));
    }

    @Test
    void testEqualsWithNullElements() {
        String[] arrayWithNulls1 = new String[]{"test", null, "data"};
        String[] arrayWithNulls2 = new String[]{"test", null, "data"};

        StringArray array1 = new StringArray(arrayWithNulls1);
        StringArray array2 = new StringArray(arrayWithNulls2);

        assertFalse(array1.equals(array2));
    }

    @Test
    void testEqualsDifferentLength() {
        String[] shorterArray = new String[]{"apple", "banana"};
        StringArray array1 = new StringArray(testData);
        StringArray array2 = new StringArray(shorterArray);

        assertFalse(array1.equals(array2));
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = stringArray.hashCode();
        int hashCode2 = stringArray.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeWithNullElements() {
        String[] arrayWithNulls = new String[]{"test", null, "data"};
        StringArray array = new StringArray(arrayWithNulls);

        assertDoesNotThrow(array::hashCode);
        int hashCode = array.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    void testHashCodeDifferentForDifferentContent() {
        StringArray array1 = new StringArray(new String[]{"one", "two"});
        StringArray array2 = new StringArray(new String[]{"three", "four"});

        assertNotEquals(array1.hashCode(), array2.hashCode());
    }

    @Test
    void testImmutability() {
        testData[0] = "modified";
        assertEquals("apple", stringArray.getArray()[0]);

        String[] internalArray = stringArray.getArray();
        internalArray[0] = "modified again";
        assertEquals("apple", stringArray.getArray()[0]);
    }

    @Test
    void testSingleElementArray() {
        String[] singleElement = new String[]{"single"};
        StringArray array = new StringArray(singleElement);

        assertEquals(1, array.length());
        assertFalse(array.isEmpty());
        assertEquals("single", array.getArray()[0]);
    }

    @Test
    void testArrayWithEmptyStrings() {
        String[] emptyStrings = new String[]{"", "", ""};
        StringArray array = new StringArray(emptyStrings);

        assertEquals(3, array.length());
        assertEquals("", array.getArray()[0]);
        assertEquals("", array.getArray()[1]);
        assertEquals("", array.getArray()[2]);
    }
}