package com.filippovich.arrayapp.reader;

import com.filippovich.arrayapp.exception.FileReadException;
import com.filippovich.arrayapp.exception.InvalidDataException;

import java.util.List;

public interface ArrayFileReader {

    List<String> readValidLinesFromFile() throws FileReadException, InvalidDataException;

    void printFileStatistics();

}