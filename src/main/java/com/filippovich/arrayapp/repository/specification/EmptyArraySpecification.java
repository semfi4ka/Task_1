package com.filippovich.arrayapp.repository.specification;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.repository.Specification;


public class EmptyArraySpecification implements Specification {
    @Override
    public boolean specified(StringArray array) {
        return array.isEmpty();
    }
}