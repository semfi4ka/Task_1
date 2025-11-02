package com.filippovich.arrayapp.service;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.exception.InvalidArrayException;

public interface ArrayService {

    String findShortestWord(StringArray array);
    String findLongestWord(StringArray array);

    double calculateAverageLength(StringArray array);
    int calculateTotalCharacters(StringArray array);

    int countWordsLongerThan(StringArray array, int minLength);
    int countWordsShorterThan(StringArray array, int maxLength);

    StringArray replaceWords(StringArray array, String oldWord, String newWord) throws InvalidArrayException;
    StringArray replaceWordsByLength(StringArray array, int targetLength, String newWord) throws InvalidArrayException;

    String findFirstAlphabetically(StringArray array);
    String findLastAlphabetically(StringArray array);
    int countWordsStartingWith(StringArray array, char letter);
    int countWordsEndingWith(StringArray array, char letter);
}