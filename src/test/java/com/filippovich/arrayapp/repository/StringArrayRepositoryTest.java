package test.java.com.filippovich.arrayapp.repository;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.factory.ArrayFactory;
import com.filippovich.arrayapp.observer.Observer;
import com.filippovich.arrayapp.repository.Specification;
import com.filippovich.arrayapp.repository.impl.StringArrayRepositoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class StringArrayRepositoryTest {

    private StringArrayRepositoryImpl repository;
    private StringArray testArray1;
    private StringArray testArray2;
    private StringArray testArray3;
    private TestObserver testObserver;

    private static class TestObserver implements Observer {
        private StringArray lastNotifiedArray;
        private String lastEventType;
        private int notificationCount = 0;

        @Override
        public void handleEvent(StringArray array, String eventType) {
            this.lastNotifiedArray = array;
            this.lastEventType = eventType;
            this.notificationCount++;
        }

        public void reset() {
            lastNotifiedArray = null;
            lastEventType = null;
            notificationCount = 0;
        }
    }

    private static class TestSpecification implements Specification {
        private final int minLength;

        public TestSpecification(int minLength) {
            this.minLength = minLength;
        }

        @Override
        public boolean specified(StringArray array) {
            return array.length() >= minLength;
        }
    }

    @Before
    public void setUp() throws InvalidArrayException {
        repository = StringArrayRepositoryImpl.getInstance();

        testArray1 = ArrayFactory.createFromArray(new String[]{"apple", "banana"});
        testArray2 = ArrayFactory.createFromArray(new String[]{"cat", "dog", "elephant"});
        testArray3 = ArrayFactory.createFromArray(new String[]{"a"});

        testObserver = new TestObserver();

        repository.clear();
        repository.clearObservers();
        repository.addObserver(testObserver);
    }

    @After
    public void tearDown() {
        repository.clear();
        repository.clearObservers();
    }

    @Test
    public void testSingletonInstance() {
        StringArrayRepositoryImpl instance1 = StringArrayRepositoryImpl.getInstance();
        StringArrayRepositoryImpl instance2 = StringArrayRepositoryImpl.getInstance();

        assertSame("Should return the same singleton instance", instance1, instance2);
    }

    @Test
    public void testAddArray() {
        repository.add(testArray1);

        List<StringArray> allArrays = repository.getAll();
        assertEquals(1, allArrays.size());
        assertTrue("Should contain added array", containsArrayById(allArrays, testArray1.getId()));

        assertEquals(1, testObserver.notificationCount);
        assertEquals(testArray1, testObserver.lastNotifiedArray);
        assertEquals("ADD", testObserver.lastEventType);
    }

    @Test
    public void testAddNullArray() {
        repository.add(null);

        List<StringArray> allArrays = repository.getAll();
        assertEquals(0, allArrays.size());

        assertEquals(0, testObserver.notificationCount);
    }

    @Test
    public void testAddMultipleArrays() {
        repository.add(testArray1);
        repository.add(testArray2);
        repository.add(testArray3);

        List<StringArray> allArrays = repository.getAll();
        assertEquals(3, allArrays.size());
        assertTrue(containsArrayById(allArrays, testArray1.getId()));
        assertTrue(containsArrayById(allArrays, testArray2.getId()));
        assertTrue(containsArrayById(allArrays, testArray3.getId()));

        assertEquals(3, testObserver.notificationCount);
    }

    @Test
    public void testRemoveArray() {
        repository.add(testArray1);
        repository.add(testArray2);

        boolean removed = repository.remove(testArray1);

        assertTrue("Should return true when array is removed", removed);

        List<StringArray> allArrays = repository.getAll();
        assertEquals(1, allArrays.size());
        assertFalse("Should not contain removed array", containsArrayById(allArrays, testArray1.getId()));
        assertTrue("Should still contain other array", containsArrayById(allArrays, testArray2.getId()));

        assertEquals(3, testObserver.notificationCount);
        assertEquals(testArray1, testObserver.lastNotifiedArray);
        assertEquals("REMOVE", testObserver.lastEventType);
    }

    @Test
    public void testRemoveNonExistentArray() {
        repository.add(testArray1);

        boolean removed = repository.remove(testArray2);

        assertFalse("Should return false when array is not found", removed);

        List<StringArray> allArrays = repository.getAll();
        assertEquals(1, allArrays.size());
        assertTrue("Should still contain original array", containsArrayById(allArrays, testArray1.getId()));
    }

    @Test
    public void testRemoveNullArray() {
        repository.add(testArray1);

        boolean removed = repository.remove(null);

        assertFalse("Should return false when removing null", removed);

        List<StringArray> allArrays = repository.getAll();
        assertEquals(1, allArrays.size());
    }

    @Test
    public void testClearRepository() {
        repository.add(testArray1);
        repository.add(testArray2);
        repository.add(testArray3);

        repository.clear();

        List<StringArray> allArrays = repository.getAll();
        assertTrue("Repository should be empty after clear", allArrays.isEmpty());

        assertEquals(6, testObserver.notificationCount);
        assertEquals("REMOVE", testObserver.lastEventType);
    }

    @Test
    public void testClearEmptyRepository() {
        repository.clear();

        List<StringArray> allArrays = repository.getAll();
        assertTrue("Repository should be empty", allArrays.isEmpty());

        assertEquals(0, testObserver.notificationCount);
    }

    @Test
    public void testQueryWithSpecification() {
        repository.add(testArray1); // length 2
        repository.add(testArray2); // length 3
        repository.add(testArray3); // length 1

        Specification spec = new TestSpecification(2);
        List<StringArray> result = repository.query(spec);

        assertEquals(2, result.size());
        assertTrue(containsArrayById(result, testArray1.getId()));
        assertTrue(containsArrayById(result, testArray2.getId()));
        assertFalse(containsArrayById(result, testArray3.getId()));
    }

    @Test
    public void testQueryWithNoMatches() {
        repository.add(testArray1);
        repository.add(testArray2);
        repository.add(testArray3);

        Specification spec = new TestSpecification(10);
        List<StringArray> result = repository.query(spec);

        assertTrue("Should return empty list when no matches", result.isEmpty());
    }

    @Test
    public void testQueryEmptyRepository() {
        Specification spec = new TestSpecification(1);
        List<StringArray> result = repository.query(spec);

        assertTrue("Should return empty list for empty repository", result.isEmpty());
    }

    @Test
    public void testGetAll() {
        repository.add(testArray1);
        repository.add(testArray2);

        List<StringArray> allArrays = repository.getAll();

        assertEquals(2, allArrays.size());
        assertTrue(containsArrayById(allArrays, testArray1.getId()));
        assertTrue(containsArrayById(allArrays, testArray2.getId()));

        allArrays.clear();
        List<StringArray> allArraysAfterClear = repository.getAll();
        assertEquals(2, allArraysAfterClear.size());
    }

    @Test
    public void testObserverManagement() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();

        repository.addObserver(observer1);
        repository.addObserver(observer2);

        assertEquals(3, repository.getObserverCount());

        repository.removeObserver(observer1);
        assertEquals(2, repository.getObserverCount());

        repository.clearObservers();
        assertEquals(0, repository.getObserverCount());
    }

    @Test
    public void testAddArrayWithNullId() throws InvalidArrayException {
        StringArray arrayWithNullId = new StringArray(new String[]{"test"}) {
            @Override
            public UUID getId() {
                return null;
            }
        };

        repository.add(arrayWithNullId);

        List<StringArray> allArrays = repository.getAll();
        assertEquals(0, allArrays.size());

        assertEquals(0, testObserver.notificationCount);
    }

    @Test
    public void testRemoveByDifferentInstanceWithSameId() throws InvalidArrayException {
        repository.add(testArray1);

        StringArray arrayWithSameId = new StringArray(testArray1.getArray()) {
            @Override
            public UUID getId() {
                return testArray1.getId();
            }
        };

        boolean removed = repository.remove(arrayWithSameId);

        assertTrue("Should remove array by ID even if different instance", removed);
        assertTrue("Repository should be empty after removal", repository.getAll().isEmpty());
    }

    @Test
    public void testRepositoryIsolation() {
        repository.add(testArray1);
        assertEquals(1, repository.getAll().size());
    }

    @Test
    public void testObserverNotificationOrder() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();

        repository.addObserver(observer1);
        repository.addObserver(observer2);

        repository.add(testArray1);

        assertEquals(1, observer1.notificationCount);
        assertEquals(1, observer2.notificationCount);
        assertEquals(testArray1, observer1.lastNotifiedArray);
        assertEquals(testArray1, observer2.lastNotifiedArray);
        assertEquals("ADD", observer1.lastEventType);
        assertEquals("ADD", observer2.lastEventType);
    }

    @Test
    public void testMultipleObserversGetNotified() {
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        TestObserver observer3 = new TestObserver();

        repository.addObserver(observer1);
        repository.addObserver(observer2);
        repository.addObserver(observer3);

        repository.add(testArray1);

        assertEquals(1, observer1.notificationCount);
        assertEquals(1, observer2.notificationCount);
        assertEquals(1, observer3.notificationCount);
        assertEquals(testArray1, observer1.lastNotifiedArray);
        assertEquals("ADD", observer1.lastEventType);
    }

    @Test
    public void testObserverRemoval() {
        TestObserver observerToRemove = new TestObserver();
        repository.addObserver(observerToRemove);
        assertEquals(2, repository.getObserverCount());
        repository.removeObserver(observerToRemove);
        assertEquals(1, repository.getObserverCount());
        repository.add(testArray1);
        assertEquals(0, observerToRemove.notificationCount);
        assertEquals(1, testObserver.notificationCount);
    }

    private boolean containsArrayById(List<StringArray> arrays, UUID id) {
        return arrays.stream()
                .anyMatch(array -> array != null && array.getId() != null && array.getId().equals(id));
    }
}