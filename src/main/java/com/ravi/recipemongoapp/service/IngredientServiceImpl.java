package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.IngredientCommand;
import com.ravi.recipemongoapp.converters.IngredientCommandToIngredient;
import com.ravi.recipemongoapp.converters.IngredientToIngredientCommand;
import com.ravi.recipemongoapp.domain.Ingredient;
import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import com.ravi.recipemongoapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/11/2020 */
@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 RecipeRepository recipeRepository,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()){
            // TODO: Yet to implement error handling
            log.error("Recipe id not found. Id: "+ recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommand = recipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getId().equals(ingredientId))
                        .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if (!ingredientCommand.isPresent()){
            // TODO: Yet to implement error handling
            log.error("Ingredient id not found. Id: "+ ingredientId);
        }

        return ingredientCommand.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (!recipeOptional.isPresent()){
            // TODO: Need to perform error handling
            log.error("Unable to find recipe with id: " + command.getRecipeId());
            return new IngredientCommand();
        }
        else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (!ingredientOptional.isPresent()){
                // New Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                //ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }
            else{
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository
                            .findById(command.getUom().getId())
                            .orElseThrow(()-> new RuntimeException("UOM Not Found"))); // TODO: Need to handle it better
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                    .findFirst();

            // Check by description
            if (!savedIngredientOptional.isPresent()){
                // TODO: Some validation pending
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            // Enhance with id value
            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(recipe.getId());

            // TODO: Check for failure conditions
            return ingredientCommandSaved;
        }
    }

    @Override
    public void deleteIngredientCommand(IngredientCommand command) {

        if (command == null){
            log.error("Provided Ingredient command was null");
            return;
        }

        Optional<Recipe> recipe = recipeRepository.findById(command.getRecipeId());

        if (recipe.isPresent()){
            Recipe recipeObj = recipe.get();
            Set<Ingredient> ingredients = recipeObj.getIngredients();
            for (Ingredient ingredient : ingredients){
                if(ingredient.getId() == command.getId()){
                    ingredients.remove(ingredient);
                    break;
                }
            }

            recipeRepository.save(recipeObj);
        }
        else{
            log.error("Unable to find the recipe id "+command.getRecipeId()+" for the provided ingredient");
            return;
        }
    }

    @Override
    public void deleteById(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();
            if (ingredientOptional.isPresent()){
                Ingredient ingredient = ingredientOptional.get();
                //ingredient.setRecipe(null);
                recipe.getIngredients().remove(ingredient);
                recipeRepository.save(recipe);
            }
            else {
                log.error("Provided ingredient id "+ingredientId+" not found");
                return;
            }
        }
        else{
            log.error("Unable to find the recipe id "+recipeId+" for the provided ingredient");
        }
    }
}
