package com.filippovich.arrayapp.comparator;

import com.filippovich.arrayapp.entity.StringArray;

import java.util.Comparator;

public interface StringArrayComparator {
    Comparator<StringArray> byId();
    Comparator<StringArray> byLength();
    Comparator<StringArray> byFirstElement();
    Comparator<StringArray> byLastElement();
    Comparator<StringArray> byAlphabeticalOrder();
}
