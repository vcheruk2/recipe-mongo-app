package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.RecipeCommand;
import com.ravi.recipemongoapp.domain.Recipe;

import java.util.Set;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 3/29/2020 */
public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(String id);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    RecipeCommand findCommandById(String id);
    void deleteById(String id);
}
