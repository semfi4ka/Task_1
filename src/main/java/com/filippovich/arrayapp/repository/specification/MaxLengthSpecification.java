package com.filippovich.arrayapp.repository.specification;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.repository.Specification;
import com.filippovich.arrayapp.warehouse.impl.ArrayStatisticsImpl;
import com.filippovich.arrayapp.warehouse.impl.ArrayWarehouse;

import java.util.Optional;

public class MaxLengthSpecification implements Specification {

    private final int targetMaxLength;

    public MaxLengthSpecification(int targetMaxLength) {
        this.targetMaxLength = targetMaxLength;
    }

    @Override
    public boolean specified(StringArray array) {
        ArrayWarehouse arrayWarehouse = ArrayWarehouse.getInstance();
        Optional<ArrayStatisticsImpl> stats = arrayWarehouse.getStatistics(array.getId());
        return stats.isPresent() && stats.get().getMaxLength() == targetMaxLength;
    }
}