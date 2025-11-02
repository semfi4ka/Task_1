package com.filippovich.arrayapp.repository;

import com.filippovich.arrayapp.entity.StringArray;

@FunctionalInterface
public interface Specification {
    boolean specified(StringArray array);
}