package test.java.com.filippovich.arrayapp.reader;

import com.filippovich.arrayapp.exception.FileReadException;
import com.filippovich.arrayapp.reader.impl.ArrayFileReaderImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayFileReaderTest {

    private ArrayFileReaderImpl arrayFileReader;
    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("test_data", ".txt");
        testFile.deleteOnExit();

        arrayFileReader = new ArrayFileReaderImpl(testFile.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
        clearCache();
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    private void clearCache() throws Exception {
        Field cacheField = ArrayFileReaderImpl.class.getDeclaredField("cachedLines");
        cacheField.setAccessible(true);
        cacheField.set(arrayFileReader, null);
    }

    private void writeToTestFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write(content);
        }
    }


    @Test
    public void testReadValidLinesFromFile_WithValidData() throws Exception {
        String fileContent = "apple,banana,cherry\n" +
                "dog,cat,bird\n" +
                "hello,world";
        writeToTestFile(fileContent);
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 3 valid lines", 3, result.size());
        assertEquals("First line should match", "apple,banana,cherry", result.get(0));
        assertEquals("Second line should match", "dog,cat,bird", result.get(1));
        assertEquals("Third line should match", "hello,world", result.get(2));
    }

    @Test
    public void testReadValidLinesFromFile_WithEmptyLines() throws Exception {
        String fileContent = "first,line\n" +
                "\n" +
                "third,line\n" +
                "   \n" +
                "last,line";
        writeToTestFile(fileContent);
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 3 valid lines (empty lines skipped)", 3, result.size());
        assertEquals("First line should match", "first,line", result.get(0));
        assertEquals("Second line should match", "third,line", result.get(1));
        assertEquals("Third line should match", "last,line", result.get(2));
    }
    @Test
    public void testReadValidLinesFromFile_WithSomeInvalidFormat() throws Exception {
        String fileContent = "valid,line\n" +
                "invalid@line\n" +
                "another,valid\n" +
                "line$with$symbols";
        writeToTestFile(fileContent);
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertTrue("Should have at least 2 valid lines", result.size() >= 2);
        assertTrue("Should contain valid lines",
                result.contains("valid,line") && result.contains("another,valid"));
    }

    @Test
    public void testReadValidLinesFromFile_WithMixedContent() throws Exception {
        String fileContent = "word1,word2\n" +
                "single\n" +
                "   \n" +
                "multiple,words,here\n" +
                "bad@line\n" +
                "good,line";
        writeToTestFile(fileContent);
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertTrue("Should contain word1,word2", result.contains("word1,word2"));
        assertTrue("Should contain single", result.contains("single"));
        assertTrue("Should contain multiple,words,here", result.contains("multiple,words,here"));
        assertTrue("Should contain good,line", result.contains("good,line"));
        assertFalse("Should not contain empty lines", result.contains("") || result.contains("   "));
    }

    @Test
    public void testReadValidLinesFromFile_WithTrimming() throws Exception {
        String fileContent = "  word1,word2  \n" +
                "  single  \n" +
                "  multiple  ,  words  ";
        writeToTestFile(fileContent);
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertTrue("Should contain trimmed lines",
                result.contains("word1,word2") && result.contains("single"));
    }

    @Test(expected = FileReadException.class)
    public void testReadValidLinesFromFile_FileNotFound() throws Exception {
        ArrayFileReaderImpl readerWithBadPath = new ArrayFileReaderImpl("nonexistent/file_that_does_not_exist_12345.txt");
        readerWithBadPath.readValidLinesFromFile();
    }

    @Test
    public void testReadValidLinesFromFile_EmptyFile() throws Exception {
        writeToTestFile("");
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty", result.isEmpty());
    }

    @Test
    public void testReadValidLinesFromFile_WithActuallyInvalidLines() throws Exception {
        String fileContent = "   \n" +
                "\n" +
                "\t\n" +
                "";
        writeToTestFile(fileContent);
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty with only whitespace lines", result.isEmpty());
    }

    @Test
    public void testReadValidLinesFromFile_Caching() throws Exception {
        String fileContent = "line1,line2\nline3,line4";
        writeToTestFile(fileContent);
        List<String> firstResult = arrayFileReader.readValidLinesFromFile();
        writeToTestFile("different,content");
        List<String> secondResult = arrayFileReader.readValidLinesFromFile();
        assertNotNull("First result should not be null", firstResult);
        assertNotNull("Second result should not be null", secondResult);
        assertEquals("Results should have same size", firstResult.size(), secondResult.size());
        assertEquals("First element should be the same", firstResult.get(0), secondResult.get(0));
    }

    @Test
    public void testPrintFileStatistics_WithValidFile() throws Exception {
        String fileContent = "first,line\nsecond,line\nthird,line";
        writeToTestFile(fileContent);
        arrayFileReader.printFileStatistics();
    }

    @Test
    public void testPrintFileStatistics_WithEmptyFile() throws Exception {
        writeToTestFile("");
        arrayFileReader.printFileStatistics();
    }

    @Test
    public void testReadValidLinesFromFile_AfterCacheClear() throws Exception {
        String firstContent = "first,content";
        writeToTestFile(firstContent);
        List<String> firstResult = arrayFileReader.readValidLinesFromFile();
        assertNotNull("First result should not be null", firstResult);
        String secondContent = "second,content";
        writeToTestFile(secondContent);
        clearCache();
        List<String> secondResult = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Second result should not be null", secondResult);
        assertEquals("Should read new content", "second,content", secondResult.get(0));
    }

    @Test
    public void testReadValidLinesFromFile_WithSingleValidLine() throws Exception {
        String fileContent = "single,line";
        writeToTestFile(fileContent);
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertEquals("Should have 1 line", 1, result.size());
        assertEquals("Line content should match", "single,line", result.get(0));
    }

    @Test
    public void testReadValidLinesFromFile_WithOnlyEmptyLines() throws Exception {
        String fileContent = "\n\n   \n";
        writeToTestFile(fileContent);
        List<String> result = arrayFileReader.readValidLinesFromFile();
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty when only empty lines", result.isEmpty());
    }

    @Test
    public void testReadValidLinesFromFile_BasicScenario() throws Exception {
        String fileContent = "test1,test2\n" +
                "hello\n" +
                "world,earth";
        writeToTestFile(fileContent);

        List<String> result = arrayFileReader.readValidLinesFromFile();

        assertNotNull("Result should not be null", result);
        assertTrue("Should contain test1,test2", result.contains("test1,test2"));
        assertTrue("Should contain hello", result.contains("hello"));
        assertTrue("Should contain world,earth", result.contains("world,earth"));
    }
}