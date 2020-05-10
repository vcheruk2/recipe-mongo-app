package com.ravi.recipemongoapp.repositories.reactive;

import com.ravi.recipemongoapp.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class CategoryReactiveRepositoryTest {

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @BeforeEach
    void setUp() {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    void testSave() {
        Category category = new Category();
        category.setDescription("Test Category");

        categoryReactiveRepository.save(category).block();

        assertEquals(1L, categoryReactiveRepository.count().block());
    }

    @Test
    void findByDescription() {
        String desc = "Test Category";

        Category category = new Category();
        category.setDescription(desc);

        categoryReactiveRepository.save(category).block();

        assertEquals(desc, categoryReactiveRepository.findByDescription(desc).block().getDescription());
    }
}