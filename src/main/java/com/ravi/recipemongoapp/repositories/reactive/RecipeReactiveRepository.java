package com.ravi.recipemongoapp.repositories.reactive;

import com.ravi.recipemongoapp.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 5/5/2020 */
public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
