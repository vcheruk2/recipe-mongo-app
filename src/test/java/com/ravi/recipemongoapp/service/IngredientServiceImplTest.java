package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.IngredientCommand;
import com.ravi.recipemongoapp.converters.IngredientCommandToIngredient;
import com.ravi.recipemongoapp.converters.IngredientToIngredientCommand;
import com.ravi.recipemongoapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.ravi.recipemongoapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.ravi.recipemongoapp.domain.Ingredient;
import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import com.ravi.recipemongoapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientServiceImpl ingredientServiceImpl;


    public IngredientServiceImplTest(){
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientServiceImpl = new IngredientServiceImpl(ingredientToIngredientCommand,
                                                recipeRepository,
                                                ingredientCommandToIngredient,
                                                unitOfMeasureRepository);
    }

    @Test
    void findByRecipeIdAndIngredientIdTrueCase() throws Exception {
        // given
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2");
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        // when
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        // then
        IngredientCommand ingredientCommand = ingredientServiceImpl.findByRecipeIdAndIngredientId("1", "3");

        assertEquals("3", ingredientCommand.getId());
        assertEquals("1", ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test
    void saveIngredient() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId("3");
        command.setRecipeId("2");

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("3");

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        //when
        IngredientCommand savedCommand = ingredientServiceImpl.saveIngredientCommand(command);

        //then
        assertEquals("3", savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void deleteIngredientCommand() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("2");

        Ingredient ingredient = new Ingredient();
        ingredient.setId("2");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("1");

        recipe.addIngredient(ingredient);
        recipe.addIngredient(ingredient2);
        assertEquals(2, recipe.getIngredients().size());

        IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient);

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        ingredientServiceImpl.deleteIngredientCommand(ingredientCommand);
        verify(recipeRepository, times(1)).findById(anyString());
        assertEquals(1, recipe.getIngredients().size());
    }

    @Test
    public void deleteIngredientById() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("2");

        Ingredient ingredient = new Ingredient();
        ingredient.setId("2");
        ingredient.setRecipe(recipe);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("1");
        ingredient2.setRecipe(recipe);

        recipe.addIngredient(ingredient);
        recipe.addIngredient(ingredient2);
        assertEquals(2, recipe.getIngredients().size());

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        ingredientServiceImpl.deleteById("2", "2");
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        assertEquals(1, recipe.getIngredients().size());
    }
}