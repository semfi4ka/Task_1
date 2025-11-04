package com.filippovich.arrayapp.repository.specification;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.repository.Specification;

public class LengthSpecification implements Specification {
    private final int targetLength;

    public LengthSpecification(int targetLength) {this.targetLength = targetLength;}

    @Override
    public boolean specified(StringArray array) {
        return array.length() == targetLength;
    }
}