package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.IngredientCommand;
import com.ravi.recipemongoapp.converters.IngredientCommandToIngredient;
import com.ravi.recipemongoapp.converters.IngredientToIngredientCommand;
import com.ravi.recipemongoapp.domain.Ingredient;
import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import com.ravi.recipemongoapp.repositories.reactive.RecipeReactiveRepository;
import com.ravi.recipemongoapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/11/2020 */
@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 RecipeReactiveRepository recipeReactiveRepository,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeReactiveRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    @Transactional
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Recipe recipe = recipeReactiveRepository.findById(command.getRecipeId()).block();

        if (recipe == null){
            // TODO: Need to perform error handling
            log.error("Unable to find recipe with id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
        }
        else {
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
                ingredientFound.setUom(unitOfMeasureReactiveRepository
                            .findById(command.getUom().getId()).block());
                            //.orElseThrow(()-> new RuntimeException("UOM Not Found"))); // TODO: Need to handle it better

                if (ingredientFound.getUom() == null)
                    new RuntimeException("UOM NOT FOUND");
            }

            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                    .findFirst();

            // Check by description
            if (!savedIngredientOptional.isPresent()){
                // TODO: Some validation pending

                log.debug("Ingredient not found from recipe so we are guessing based on properties");
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
            return Mono.just(ingredientCommandSaved);
        }
    }

    @Override
    public Mono<Void> deleteIngredientCommand(IngredientCommand command) {

        if (command == null){
            log.error("Provided Ingredient command was null");
            return Mono.empty();
        }

        Recipe recipe = recipeReactiveRepository.findById(command.getRecipeId()).block();

        if (recipe != null){
            Optional<Ingredient> ingredient = recipe.getIngredients()
                                        .stream()
                                        .filter(ingredient1 -> ingredient1.getId().equalsIgnoreCase(command.getId()))
                                        .findFirst();
            if (ingredient.isPresent()){
                recipe.getIngredients().remove(ingredient.get());
                recipeReactiveRepository.save(recipe).block();
            }
        }
        else
            log.error("Unable to find the recipe id "+command.getRecipeId()+" for the provided ingredient");

        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {

        Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe != null){
            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                log.debug("Ingredient is present");

                recipe.getIngredients().remove(ingredientOptional.get());
                recipeReactiveRepository.save(recipe).block();
            }
            else
                log.debug("Ingredient id "+ ingredientId + "not found in recipe id " + recipeId);
        }
        else
            log.debug("Recipe id "+ recipeId + " not found");

        return Mono.empty();
    }
}
