package com.ravi.recipemongoapp.repositories.reactive;

import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UnitOfMeasureReactiveRepositoryTest {

    public static final String DESCRIPTION = "New UOM";

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    UnitOfMeasure unitOfMeasure, unitOfMeasureSaved;

    @BeforeEach
    void setUp() {
        unitOfMeasureReactiveRepository.deleteAll().block();
        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription(DESCRIPTION);
        unitOfMeasureSaved = unitOfMeasureReactiveRepository.save(unitOfMeasure).block();
    }

    @Test
    public void testSave() {
        assertEquals(1L, unitOfMeasureReactiveRepository.count().block());
    }

    @Test
    void findByDescription() {
        assertEquals(DESCRIPTION, unitOfMeasureReactiveRepository.findByDescription(DESCRIPTION).block().getDescription());
    }
}