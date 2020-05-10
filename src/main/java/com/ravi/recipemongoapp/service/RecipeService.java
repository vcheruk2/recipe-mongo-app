package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.RecipeCommand;
import com.ravi.recipemongoapp.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 3/29/2020 */
public interface RecipeService {
    Flux<Recipe> getRecipes();
    Mono<Recipe> findById(String id);
    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);
    Mono<RecipeCommand> findCommandById(String id);
    Mono<Void> deleteById(String id);
}
