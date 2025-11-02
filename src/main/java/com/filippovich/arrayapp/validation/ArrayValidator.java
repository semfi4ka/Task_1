package com.filippovich.arrayapp.validation;

public interface ArrayValidator {
    String WORD_REGEX = "[a-zA-Zа-яА-Я]+";
    String DELIMITER_REGEX = "[,\\s;\\-]+";

    boolean validateArray(String[] stringArray);
    boolean isValidWordString(String wordString);
    boolean validateLineFormat(String wordsLine);
}