package test.java.com.filippovich.arrayapp.parser;

import com.filippovich.arrayapp.parser.impl.ArrayParserImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayParserTest {

    private ArrayParserImpl arrayParser;

    @BeforeEach
    void setUp() {
        arrayParser = new ArrayParserImpl();
    }

    @Test
    void testParseStringToArray_NullInput() {
        String[] result = arrayParser.parseStringToArray(null);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testParseStringToArray_EmptyString() {
        String[] result = arrayParser.parseStringToArray("");
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testParseStringToArray_BlankString() {
        String[] result = arrayParser.parseStringToArray("   ");
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testParseStringToArray_SingleValidWord() {
        String[] result = arrayParser.parseStringToArray("hello");
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("hello", result[0]);
    }

    @Test
    void testParseStringToArray_MultipleValidWords() {
        String input = "hello,world,test";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }

    @Test
    void testParseStringToArray_WithSpacesAroundDelimiters() {
        String input = "hello , world , test";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }

    @Test
    void testParseStringToArray_WithMultipleDelimiters() {
        String input = "hello,,world,,test";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }

    @Test
    void testParseStringToArray_WithLeadingTrailingDelimiters() {
        String input = ",hello,world,test,";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }

    @Test
    void testParseStringToArray_WithInvalidWords() {
        String input = "hello,123,world,test456";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    void testParseStringToArray_WithMixedCase() {
        String input = "Hello,WORLD,Test";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"Hello", "WORLD", "Test"}, result);
    }

    @Test
    void testParseStringToArray_WithSpecialCharacters() {
        String input = "hello-world,test_word,simple";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertDoesNotThrow(() -> arrayParser.parseStringToArray(input));
    }

    @Test
    void testParseStringToArray_OnlyInvalidWords() {
        String input = "123,456,789";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testParseStringToArray_WithTabsAndNewlines() {
        String input = "hello\t,\nworld,\ttest\n";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"hello", "world", "test"}, result);
    }

    @Test
    void testParseStringToArray_ComplexScenario() {
        String input = "  apple ,, banana ,,123, cherry456 , grape ,  ";
        String[] result = arrayParser.parseStringToArray(input);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(new String[]{"apple", "banana", "grape"}, result);
    }

    @Test
    void testParseStringToArray_ReturnNewArrayInstance() {
        String input = "test1,test2";
        String[] result1 = arrayParser.parseStringToArray(input);
        String[] result2 = arrayParser.parseStringToArray(input);

        assertNotSame(result1, result2);
        assertArrayEquals(result1, result2);
    }

    @Test
    void testParseStringToArray_PreservesWordOrder() {
        String input = "first,second,third,fourth";
        String[] result = arrayParser.parseStringToArray(input);

        assertEquals("first", result[0]);
        assertEquals("second", result[1]);
        assertEquals("third", result[2]);
        assertEquals("fourth", result[3]);
    }

    @Test
    void testParseStringToArray_TrimsSpacesFromWords() {
        String input = "  hello  ,  world  ,  test  ";
        String[] result = arrayParser.parseStringToArray(input);

        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
        assertEquals("test", result[2]);
    }
}