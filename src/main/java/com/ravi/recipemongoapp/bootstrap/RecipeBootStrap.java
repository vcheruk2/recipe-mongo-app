package com.ravi.recipemongoapp.bootstrap;

import com.ravi.recipemongoapp.domain.*;
import com.ravi.recipemongoapp.repositories.CategoryRepository;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import com.ravi.recipemongoapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 3/29/2020 */
@Component
@Slf4j
@Profile("default")
public class RecipeBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootStrap(RecipeRepository recipeRepository,
                           CategoryRepository categoryRepository,
                           UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Optional<UnitOfMeasure> ounceUom = unitOfMeasureRepository.findByDescription("Each");
        if(!ounceUom.isPresent())
            throw new RuntimeException("Expected UOM for each is not found");

        Optional<UnitOfMeasure> teaSpoonUom = unitOfMeasureRepository.findByDescription("Teaspoon");
        if (!teaSpoonUom.isPresent())
            throw new RuntimeException("Expected UOM for teaspoon is not found");

        Optional<Category> american = categoryRepository.findByDescription("American");
        if (!american.isPresent())
            throw new RuntimeException("Expected Category American is not found");

        Optional<Category> mexican = categoryRepository.findByDescription("Mexican");
        if(!mexican.isPresent())
            throw new RuntimeException("Expected Category Mexican is not found");

        // Guacamole Recipe
        Recipe guac = new Recipe();

        Ingredient avocados = new Ingredient("Avocados", BigDecimal.valueOf(2), ounceUom.get());
        Ingredient salt = new Ingredient("Salt", BigDecimal.valueOf(0.25), teaSpoonUom.get());

        Notes guacNotes = new Notes();
        //guacNotes.setRecipe(guac);
        guacNotes.setRecipeNotes("Guac Recipe Notes");

        guac.setDescription("Perfect Guacamole");
        guac.setCookTime(10);
        guac.setPrepTime(10);
        guac.setDifficulty(Difficulty.EASY);
        guac.setDirections("Directions to make Guac");
        guac.setNotes(guacNotes);
        guac.setServings(4);
        guac.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guac.setSource("Simply Recipes");

        guac.addIngredient(avocados);
        guac.addIngredient(salt);

        Set<Category> guacCategories = new HashSet<>();
        guacCategories.add(american.get());
        guacCategories.add(mexican.get());
        guac.setCategories(guacCategories);

        recipeRepository.save(guac);

        log.debug("Created Guacamole recipe and saved it to the repo");

        // Chicken Taco
        Recipe chickenTaco = new Recipe();
        chickenTaco.setDirections("Making of Chicken Taco");
        chickenTaco.setDescription("Spicy Chicken Taco");
        chickenTaco.setDifficulty(Difficulty.MODERATE);
        chickenTaco.setSource("Simply Recipes");
        chickenTaco.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        chickenTaco.setServings(6);
        chickenTaco.setCookTime(15);
        chickenTaco.setPrepTime(20);

        Notes chickenTacoNotes = new Notes();
        chickenTaco.setNotes(chickenTacoNotes);
        chickenTacoNotes.setRecipeNotes("Chicken Taco Recipe Notes");

        Ingredient oil = new Ingredient("Chicken Taco Oil", BigDecimal.valueOf(2),
                            teaSpoonUom.get() );

        Ingredient chicken = new Ingredient("Chicken", BigDecimal.valueOf(6),
                                ounceUom.get());

        chickenTaco.addIngredient(oil);
        chickenTaco.addIngredient(chicken);

        HashSet<Category> chickenTacoCategories = new HashSet<>();
        chickenTacoCategories.add(american.get());
        chickenTacoCategories.add(mexican.get());
        chickenTaco.setCategories(chickenTacoCategories);

        recipeRepository.save(chickenTaco);

        log.debug("Created Chicken Taco and saved it to the repository");
    }
}
