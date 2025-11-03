package test.java.com.filippovich.arrayapp.warehouse;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.warehouse.impl.ArrayStatisticsImpl;
import com.filippovich.arrayapp.warehouse.impl.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static java.lang.Math.round;
import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    private Warehouse warehouse;
    private StringArray testArray;
    private StringArray emptyArray;
    private StringArray singleElementArray;

    @BeforeEach
    void setUp() {
        warehouse = Warehouse.getInstance();
        testArray = new StringArray(new String[]{"hello", "world", "test"});
        emptyArray = new StringArray(new String[]{});
        singleElementArray = new StringArray(new String[]{"single"});
    }

    @AfterEach
    void tearDown() {
        warehouse.clearStatistics();
    }

    @Test
    void testGetInstance() {
        assertNotNull(warehouse);

        Warehouse anotherInstance = Warehouse.getInstance();
        assertSame(warehouse, anotherInstance);
    }

    @Test
    void testHandleEventAdd() {
        warehouse.handleEvent(testArray, "ADD");

        Optional<ArrayStatisticsImpl> stats = warehouse.getStatistics(testArray.getId());
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
        warehouse.handleEvent(emptyArray, "ADD");

        Optional<ArrayStatisticsImpl> stats = warehouse.getStatistics(emptyArray.getId());
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
        warehouse.handleEvent(singleElementArray, "ADD");

        Optional<ArrayStatisticsImpl> stats = warehouse.getStatistics(singleElementArray.getId());
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
        warehouse.handleEvent(testArray, "ADD");
        assertTrue(warehouse.getStatistics(testArray.getId()).isPresent());

        warehouse.handleEvent(testArray, "REMOVE");
        assertFalse(warehouse.getStatistics(testArray.getId()).isPresent());
    }

    @Test
    void testHandleEventRemoveNonExistent() {
        UUID nonExistentId = UUID.randomUUID();
        StringArray nonExistentArray = new StringArray(new String[]{"test"});

        assertDoesNotThrow(() -> warehouse.handleEvent(nonExistentArray, "REMOVE"));
    }

    @Test
    void testHandleEventUnknownEventType() {
        warehouse.handleEvent(testArray, "UNKNOWN_EVENT");

        assertFalse(warehouse.getStatistics(testArray.getId()).isPresent());
    }

    @Test
    void testHandleEventNullArray() {
        assertDoesNotThrow(() -> warehouse.handleEvent(null, "ADD"));
        assertDoesNotThrow(() -> warehouse.handleEvent(null, "REMOVE"));
    }

    @Test
    void testGetStatisticsNonExistent() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<ArrayStatisticsImpl> stats = warehouse.getStatistics(nonExistentId);

        assertFalse(stats.isPresent());
    }

    @Test
    void testClearStatistics() {
        warehouse.handleEvent(testArray, "ADD");
        warehouse.handleEvent(emptyArray, "ADD");
        warehouse.handleEvent(singleElementArray, "ADD");

        assertTrue(warehouse.getStatistics(testArray.getId()).isPresent());
        assertTrue(warehouse.getStatistics(emptyArray.getId()).isPresent());
        assertTrue(warehouse.getStatistics(singleElementArray.getId()).isPresent());

        warehouse.clearStatistics();

        assertFalse(warehouse.getStatistics(testArray.getId()).isPresent());
        assertFalse(warehouse.getStatistics(emptyArray.getId()).isPresent());
        assertFalse(warehouse.getStatistics(singleElementArray.getId()).isPresent());
    }

    @Test
    void testMultipleArraysStatistics() {
        StringArray array1 = new StringArray(new String[]{"a", "bb", "ccc"});
        StringArray array2 = new StringArray(new String[]{"xxxx", "yyyyy"});

        warehouse.handleEvent(array1, "ADD");
        warehouse.handleEvent(array2, "ADD");

        Optional<ArrayStatisticsImpl> stats1 = warehouse.getStatistics(array1.getId());
        assertTrue(stats1.isPresent());
        assertEquals(2.0, stats1.get().getAverageLength(), 0.001);
        assertEquals(6, stats1.get().getTotalCharacters());
        assertEquals(3, stats1.get().getMaxLength());
        assertEquals(1, stats1.get().getMinLength());
        assertEquals(3, stats1.get().getWordCount());

        Optional<ArrayStatisticsImpl> stats2 = warehouse.getStatistics(array2.getId());
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

        warehouse.handleEvent(variedArray, "ADD");

        Optional<ArrayStatisticsImpl> stats = warehouse.getStatistics(variedArray.getId());
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

        warehouse.handleEvent(array, "ADD");
        Optional<ArrayStatisticsImpl> firstStats = warehouse.getStatistics(array.getId());
        assertTrue(firstStats.isPresent());
        assertEquals(3.0, firstStats.get().getAverageLength(), 0.001);

        warehouse.handleEvent(array, "ADD");
        Optional<ArrayStatisticsImpl> secondStats = warehouse.getStatistics(array.getId());
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