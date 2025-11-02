package com.filippovich.arrayapp.reader.impl;

import com.filippovich.arrayapp.reader.ArrayFileReader;
import com.filippovich.arrayapp.validation.impl.ArrayValidatorImpl;
import com.filippovich.arrayapp.exception.FileReadException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayFileReaderImpl implements ArrayFileReader {
    private static final Logger logger = LogManager.getLogger(ArrayFileReaderImpl.class);
    private static final String FILE_PATH = "data/words.txt";
    private List<String> cachedLines;

    public ArrayFileReaderImpl() {
        this.cachedLines = null;
    }

    @Override
    public List<String> readValidLinesFromFile() throws FileReadException {
        if (cachedLines != null) {
            logger.debug("Returning cached lines");
            return new ArrayList<>(cachedLines);
        }

        logger.info("Reading and validating lines from file: {}", FILE_PATH);
        List<String> validLines = new ArrayList<>();
        ArrayValidatorImpl validator = new ArrayValidatorImpl();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber = 0;
            int validLinesCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (processLine(line, lineNumber, validLines, validator)) {
                    validLinesCount++;
                }
            }

            cachedLines = new ArrayList<>(validLines);

            logger.info("Successfully processed {} valid lines from {} total lines",
                    validLinesCount, lineNumber);

        } catch (IOException e) {
            logger.error("Error reading file: {} - {}", FILE_PATH, e.getMessage(), e);
            throw new FileReadException("File not found or cannot be read: " + FILE_PATH, e);
        }

        return validLines;
    }

    private boolean processLine(String line, int lineNumber, List<String> validLines, ArrayValidatorImpl validator) {
        try {
            if (line == null || line.isBlank()) {
                logger.debug("Line {}: Empty line - skipped", lineNumber);
                return false;
            }

            logger.debug("Validating line {}: '{}'", lineNumber, line);

            validator.validateLineFormat(line);

            validLines.add(line.trim());
            logger.debug("Line {}: Validation successful", lineNumber);
            return true;
        } catch (Exception e) {
            logger.error("Line {}: Unexpected validation error", lineNumber, e);
            return false;
        }
    }

    @Override
    public void printFileStatistics() {
        try {
            List<String> validLines = readValidLinesFromFile();

            logger.info("=== FILE STATISTICS ===");
            logger.info("Total valid lines: {}", validLines.size());

            if (validLines.isEmpty()) {
                logger.info("No valid lines found in file!");
                return;
            }

            for (int i = 0; i < validLines.size(); i++) {
                String line = validLines.get(i);
                logger.info("Line {}: '{}'", i + 1, line);
            }
        } catch (Exception e) {
            logger.error("Error generating statistics: {}", e.getMessage(), e);
        }
    }

}