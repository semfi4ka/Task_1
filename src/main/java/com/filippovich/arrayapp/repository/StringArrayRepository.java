package com.filippovich.arrayapp.repository;

import com.filippovich.arrayapp.entity.StringArray;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StringArrayRepository {
    void add(StringArray stringArray);
    List<StringArray> getAll();
    boolean remove(StringArray stringArray);
    public void clear();

    List<StringArray> query(Specification spec);
}