package com.filippovich.arrayapp.validation.impl;

import com.filippovich.arrayapp.validation.ArrayValidator;

public final class ArrayValidatorImpl implements ArrayValidator {

    public ArrayValidatorImpl() {
    }

    @Override
    public boolean validateArray(String[] stringArray) {
        if (stringArray == null) {
            return false;
        }

        if (stringArray.length == 0) {
            return false;
        }

        for (String word : stringArray) {
            if (word == null || word.isBlank() || !isValidWordString(word.trim())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isValidWordString(String wordString) {
        return wordString != null && wordString.matches(WORD_REGEX);
    }

    @Override
    public boolean validateLineFormat(String wordsLine) {
        try {
            if (wordsLine == null || wordsLine.isBlank()) {
                return false;
            }
            String[] parts = wordsLine.split(DELIMITER_REGEX);
            boolean hasValidWord = false;
            for (String part : parts) {
                if (!part.isBlank() && isValidWordString(part.trim())) {
                    hasValidWord = true;
                    break;
                }
            }
            return hasValidWord;
        } catch (Exception e) {
            return false;
        }
    }
}