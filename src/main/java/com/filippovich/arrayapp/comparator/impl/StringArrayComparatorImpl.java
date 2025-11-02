package com.filippovich.arrayapp.comparator.impl;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.comparator.StringArrayComparator;

import java.util.Comparator;

public class StringArrayComparatorImpl implements StringArrayComparator {
    public StringArrayComparatorImpl() {
    }

    @Override
    public Comparator<StringArray> byId() {
        return Comparator.comparing(StringArray::getId);
    }

    @Override
    public Comparator<StringArray> byLength() {
        return Comparator.comparingInt(StringArray::length);
    }

    @Override
    public Comparator<StringArray> byFirstElement() {
        return (arr1, arr2) -> {
            if (arr1.isEmpty() && arr2.isEmpty()) return 0;
            if (arr1.isEmpty()) return -1;
            if (arr2.isEmpty()) return 1;

            String first1 = arr1.getArray()[0];
            String first2 = arr2.getArray()[0];
            return first1.compareToIgnoreCase(first2);
        };
    }

    @Override
    public Comparator<StringArray> byLastElement() {
        return (arr1, arr2) -> {
            if (arr1.isEmpty() && arr2.isEmpty()) return 0;
            if (arr1.isEmpty()) return -1;
            if (arr2.isEmpty()) return 1;

            String last1 = arr1.getArray()[arr1.length() - 1];
            String last2 = arr2.getArray()[arr2.length() - 1];
            return last1.compareToIgnoreCase(last2);
        };
    }

    @Override
    public Comparator<StringArray> byAlphabeticalOrder() {
        return (arr1, arr2) -> {
            // Сравниваем массивы по первому различному элементу
            int minLength = Math.min(arr1.length(), arr2.length());
            for (int i = 0; i < minLength; i++) {
                int comparison = arr1.getArray()[i].compareToIgnoreCase(arr2.getArray()[i]);
                if (comparison != 0) {
                    return comparison;
                }
            }
            return Integer.compare(arr1.length(), arr2.length());
        };
    }
}