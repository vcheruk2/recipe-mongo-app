package com.ravi.recipemongoapp.repositories;

import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UnitOfMeasureRepositoryTest {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    //@DirtiesContext
    void findByDescription() {
        Optional<UnitOfMeasure> optionalUnitOfMeasure = unitOfMeasureRepository.findByDescription("Teaspoon");
        assertEquals(1, optionalUnitOfMeasure.get().getId());
        assertEquals("Teaspoon", optionalUnitOfMeasure.get().getDescription());
    }

    @Test
    void findById(){
        Optional<UnitOfMeasure> optionalUnitOfMeasure = unitOfMeasureRepository.findByDescription("Tablespoon");
        assertEquals(2, optionalUnitOfMeasure.get().getId());
    }
}