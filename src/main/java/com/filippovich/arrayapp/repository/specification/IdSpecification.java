package com.filippovich.arrayapp.repository.specification;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.repository.Specification;

import java.util.UUID;

public class IdSpecification implements Specification {
    private final UUID id;

    public IdSpecification(UUID id) {
        this.id = id;
    }

    @Override
    public boolean specified(StringArray array) {
        return array.getId().equals(id);
    }
}