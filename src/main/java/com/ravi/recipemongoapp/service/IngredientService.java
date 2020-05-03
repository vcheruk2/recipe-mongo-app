package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.IngredientCommand;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/11/2020 */
public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    void deleteIngredientCommand(IngredientCommand command);
    void deleteById(String recipeId, String ingredientId);
}
