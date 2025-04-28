package com.ddukbbegi.api.store.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractValidatorTest {

    private ValidatorFactory factory;
    protected Validator validator;

    @BeforeEach
    void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void tearDownValidator() {
        if (factory != null) {
            factory.close();
        }
    }

}
