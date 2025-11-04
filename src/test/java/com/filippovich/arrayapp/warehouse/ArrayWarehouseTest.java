package test.java.com.filippovich.arrayapp.warehouse;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.warehouse.impl.ArrayStatisticsImpl;
import com.filippovich.arrayapp.warehouse.impl.ArrayWarehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static java.lang.Math.round;
import static org.junit.jupiter.api.Assertions.*;

class ArrayWarehouseTest {

    private ArrayWarehouse arrayWarehouse;
    private StringArray testArray;
    private StringArray emptyArray;
    private StringArray singleElementArray;

    @BeforeEach
    void setUp() {
        arrayWarehouse = ArrayWarehouse.getInstance();
        testArray = new StringArray(new String[]{"hello", "world", "test"});
        emptyArray = new StringArray(new String[]{});
        singleElementArray = new StringArray(new String[]{"single"});
    }

    @AfterEach
    void tearDown() {
        arrayWarehouse.clearStatistics();
    }

    @Test
    void testGetInstance() {
        assertNotNull(arrayWarehouse);

        ArrayWarehouse anotherInstance = ArrayWarehouse.getInstance();
        assertSame(arrayWarehouse, anotherInstance);
    }

    @Test
    void testHandleEventAdd() {
        arrayWarehouse.handleEvent(testArray, "ADD");

        Optional<ArrayStatisticsImpl> stats = arrayWarehouse.getStatistics(testArray.getId());
        assertTrue(stats.isPresent());

        ArrayStatisticsImpl statistics = stats.get();
        assertEquals(5, round(statistics.getAverageLength()));
        assertEquals(14, statistics.getTotalCharacters());
        assertEquals(5, statistics.getMaxLength());
        assertEquals(4, statistics.getMinLength());
        assertEquals(3, statistics.getWordCount());
    }

    @Test
    void testHandleEventAddEmptyArray() {
        arrayWarehouse.handleEvent(emptyArray, "ADD");

        Optional<ArrayStatisticsImpl> stats = arrayWarehouse.getStatistics(emptyArray.getId());
        assertTrue(stats.isPresent());

        ArrayStatisticsImpl statistics = stats.get();
        assertEquals(0, statistics.getAverageLength(), 0.001);
        assertEquals(0, statistics.getTotalCharacters());
        assertEquals(0, statistics.getMaxLength());
        assertEquals(0, statistics.getMinLength());
        assertEquals(0, statistics.getWordCount());
    }

    @Test
    void testHandleEventAddSingleElementArray() {
        arrayWarehouse.handleEvent(singleElementArray, "ADD");

        Optional<ArrayStatisticsImpl> stats = arrayWarehouse.getStatistics(singleElementArray.getId());
        assertTrue(stats.isPresent());

        ArrayStatisticsImpl statistics = stats.get();
        assertEquals(6.0, statistics.getAverageLength(), 0.001);
        assertEquals(6, statistics.getTotalCharacters());
        assertEquals(6, statistics.getMaxLength());
        assertEquals(6, statistics.getMinLength());
        assertEquals(1, statistics.getWordCount());
    }

    @Test
    void testHandleEventRemove() {
        arrayWarehouse.handleEvent(testArray, "ADD");
        assertTrue(arrayWarehouse.getStatistics(testArray.getId()).isPresent());

        arrayWarehouse.handleEvent(testArray, "REMOVE");
        assertFalse(arrayWarehouse.getStatistics(testArray.getId()).isPresent());
    }

    @Test
    void testHandleEventRemoveNonExistent() {
        UUID nonExistentId = UUID.randomUUID();
        StringArray nonExistentArray = new StringArray(new String[]{"test"});

        assertDoesNotThrow(() -> arrayWarehouse.handleEvent(nonExistentArray, "REMOVE"));
    }

    @Test
    void testHandleEventUnknownEventType() {
        arrayWarehouse.handleEvent(testArray, "UNKNOWN_EVENT");

        assertFalse(arrayWarehouse.getStatistics(testArray.getId()).isPresent());
    }

    @Test
    void testHandleEventNullArray() {
        assertDoesNotThrow(() -> arrayWarehouse.handleEvent(null, "ADD"));
        assertDoesNotThrow(() -> arrayWarehouse.handleEvent(null, "REMOVE"));
    }

    @Test
    void testGetStatisticsNonExistent() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<ArrayStatisticsImpl> stats = arrayWarehouse.getStatistics(nonExistentId);

        assertFalse(stats.isPresent());
    }

    @Test
    void testClearStatistics() {
        arrayWarehouse.handleEvent(testArray, "ADD");
        arrayWarehouse.handleEvent(emptyArray, "ADD");
        arrayWarehouse.handleEvent(singleElementArray, "ADD");

        assertTrue(arrayWarehouse.getStatistics(testArray.getId()).isPresent());
        assertTrue(arrayWarehouse.getStatistics(emptyArray.getId()).isPresent());
        assertTrue(arrayWarehouse.getStatistics(singleElementArray.getId()).isPresent());

        arrayWarehouse.clearStatistics();

        assertFalse(arrayWarehouse.getStatistics(testArray.getId()).isPresent());
        assertFalse(arrayWarehouse.getStatistics(emptyArray.getId()).isPresent());
        assertFalse(arrayWarehouse.getStatistics(singleElementArray.getId()).isPresent());
    }

    @Test
    void testMultipleArraysStatistics() {
        StringArray array1 = new StringArray(new String[]{"a", "bb", "ccc"});
        StringArray array2 = new StringArray(new String[]{"xxxx", "yyyyy"});

        arrayWarehouse.handleEvent(array1, "ADD");
        arrayWarehouse.handleEvent(array2, "ADD");

        Optional<ArrayStatisticsImpl> stats1 = arrayWarehouse.getStatistics(array1.getId());
        assertTrue(stats1.isPresent());
        assertEquals(2.0, stats1.get().getAverageLength(), 0.001);
        assertEquals(6, stats1.get().getTotalCharacters());
        assertEquals(3, stats1.get().getMaxLength());
        assertEquals(1, stats1.get().getMinLength());
        assertEquals(3, stats1.get().getWordCount());

        Optional<ArrayStatisticsImpl> stats2 = arrayWarehouse.getStatistics(array2.getId());
        assertTrue(stats2.isPresent());
        assertEquals(4.5, stats2.get().getAverageLength(), 0.001);
        assertEquals(9, stats2.get().getTotalCharacters());
        assertEquals(5, stats2.get().getMaxLength());
        assertEquals(4, stats2.get().getMinLength());
        assertEquals(2, stats2.get().getWordCount());
    }

    @Test
    void testStatisticsWithDifferentStringLengths() {
        StringArray variedArray = new StringArray(new String[]{"", "a", "12345", "abcdefghij"});

        arrayWarehouse.handleEvent(variedArray, "ADD");

        Optional<ArrayStatisticsImpl> stats = arrayWarehouse.getStatistics(variedArray.getId());
        assertTrue(stats.isPresent());

        ArrayStatisticsImpl statistics = stats.get();
        assertEquals(4.0, statistics.getAverageLength(), 0.001);
        assertEquals(16, statistics.getTotalCharacters());
        assertEquals(10, statistics.getMaxLength());
        assertEquals(0, statistics.getMinLength());
        assertEquals(4, statistics.getWordCount());
    }

    @Test
    void testUpdateArrayStatistics() {
        StringArray array = new StringArray(new String[]{"old"});

        arrayWarehouse.handleEvent(array, "ADD");
        Optional<ArrayStatisticsImpl> firstStats = arrayWarehouse.getStatistics(array.getId());
        assertTrue(firstStats.isPresent());
        assertEquals(3.0, firstStats.get().getAverageLength(), 0.001);

        arrayWarehouse.handleEvent(array, "ADD");
        Optional<ArrayStatisticsImpl> secondStats = arrayWarehouse.getStatistics(array.getId());
        assertTrue(secondStats.isPresent());

        assertEquals(firstStats.get().getAverageLength(), secondStats.get().getAverageLength(), 0.001);
        assertEquals(firstStats.get().getTotalCharacters(), secondStats.get().getTotalCharacters());
    }

    @Test
    void testToStringFormat() {
        ArrayStatisticsImpl stats = new ArrayStatisticsImpl(5.5, 55, 10, 2, 10);
        String toString = stats.toString();

        assertTrue(toString.contains("avg=5.5"));
        assertTrue(toString.contains("sum=55"));
        assertTrue(toString.contains("max=10"));
        assertTrue(toString.contains("min=2"));
        assertTrue(toString.contains("count=10"));
        assertTrue(toString.startsWith("ArrayStatisticsImpl["));
    }
}