package com.ravi.recipemongoapp.repositories.reactive;

import com.ravi.recipemongoapp.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class RecipeReactiveRepositoryTest {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @BeforeEach
    void setUp() {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void testRecipeSave() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription("Test Recipe");

        recipeReactiveRepository.save(recipe).block();

        assertEquals(1L, recipeReactiveRepository.count().block());
    }
}