package com.filippovich.arrayapp.parser.impl;

import com.filippovich.arrayapp.parser.ArrayParser;
import com.filippovich.arrayapp.validation.impl.ArrayValidatorImpl;

import java.util.ArrayList;
import java.util.List;

public class ArrayParserImpl implements ArrayParser {

    @Override
    public String[] parseStringToArray(String line) {
        if (line == null || line.isBlank()) {
            return new String[0];
        }

        String[] parts = line.split(ArrayValidatorImpl.DELIMITER_REGEX);
        List<String> words = new ArrayList<>();

        for (String part : parts) {
            String trimmedPart = part.trim();
            ArrayValidatorImpl arrayValidatorImpl = new ArrayValidatorImpl();
            if (!trimmedPart.isEmpty() &&  arrayValidatorImpl.isValidWordString(trimmedPart)) {
                words.add(trimmedPart);
            }
        }

        return words.toArray(new String[0]);
    }
}