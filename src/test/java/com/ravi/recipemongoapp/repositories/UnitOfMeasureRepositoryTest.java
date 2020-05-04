package com.ravi.recipemongoapp.repositories;

import com.ravi.recipemongoapp.bootstrap.RecipeBootStrap;
import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class UnitOfMeasureRepositoryTest {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();
        categoryRepository.deleteAll();

        RecipeBootStrap recipeBootStrap = new RecipeBootStrap(recipeRepository, categoryRepository, unitOfMeasureRepository);
        recipeBootStrap.onApplicationEvent(null);
    }

    @Test
    //@DirtiesContext
    void findByDescription() {
        Optional<UnitOfMeasure> optionalUnitOfMeasure = unitOfMeasureRepository.findByDescription("Teaspoon");
        //assertEquals(1, optionalUnitOfMeasure.get().getId());
        assertEquals("Teaspoon", optionalUnitOfMeasure.get().getDescription());
    }

    @Test
    void findByDescriptionTableSpoon(){
        Optional<UnitOfMeasure> optionalUnitOfMeasure = unitOfMeasureRepository.findByDescription("Tablespoon");
        assertEquals("Tablespoon", optionalUnitOfMeasure.get().getDescription());
    }
}