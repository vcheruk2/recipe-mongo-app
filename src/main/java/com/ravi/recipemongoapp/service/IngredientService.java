package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.IngredientCommand;
import reactor.core.publisher.Mono;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/11/2020 */
public interface IngredientService {
    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);
    Mono<Void> deleteIngredientCommand(IngredientCommand command);
    Mono<Void> deleteById(String recipeId, String ingredientId);
}
