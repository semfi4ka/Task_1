package com.filippovich.arrayapp.factory;

import com.filippovich.arrayapp.entity.StringArray;
import com.filippovich.arrayapp.exception.InvalidArrayException;
import com.filippovich.arrayapp.repository.StringArrayRepository;
import com.filippovich.arrayapp.repository.impl.StringArrayRepositoryImpl;
import com.filippovich.arrayapp.validation.impl.ArrayValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public final class ArrayFactory {
    private static final Logger logger = LogManager.getLogger(ArrayFactory.class);
    private static final StringArrayRepository repository = StringArrayRepositoryImpl.getInstance();

    private ArrayFactory() {}

    public static StringArray createFromArray(String[] array) throws InvalidArrayException {
        logger.debug("Creating StringArray from array: {}",
                array != null ? Arrays.toString(array) : "null");

        ArrayValidatorImpl arrayValidatorImpl = new ArrayValidatorImpl();
        arrayValidatorImpl.validateArray(array);

        StringArray result = new StringArray(array);

        repository.add(result);

        logger.info("Successfully created and saved StringArray with id: {}", result.getId());
        return result;
    }
}