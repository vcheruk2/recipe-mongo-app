package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.RecipeCommand;
import com.ravi.recipemongoapp.converters.RecipeCommandToRecipe;
import com.ravi.recipemongoapp.converters.RecipeToRecipeCommand;
import com.ravi.recipemongoapp.domain.Ingredient;
import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.exceptions.NotFoundException;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import com.ravi.recipemongoapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    static RecipeServiceImpl recipeService;

    @Mock
    static RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    static RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    static RecipeReactiveRepository recipeReactiveRepository;

    @BeforeEach
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(new RecipeServiceImplTest());
        recipeService = new RecipeServiceImpl(recipeReactiveRepository, recipeToRecipeCommand, recipeCommandToRecipe);
    }

    @Test
    @DirtiesContext
    public void getRecipes() throws Exception{
        when(recipeReactiveRepository.findAll()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        assertEquals(2, recipeService.getRecipes().collectList().block().size());
        verify(recipeReactiveRepository, times(1)).findAll();
    }

    @Test
    public void getRecipesByIdTest() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        recipe.setDescription("New Recipe");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        assertNotNull(recipeService.findById("1"), "Null Recipe returned");
        //assertEquals(recipe.getDescription(), recipeService.findById("1").block().getDescription());
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void getRecipesByIdCommandTest() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        recipe.setDescription("New Recipe");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById("1").block();

        assertNotNull(recipeCommand, "Null Recipe returned");
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void deleteRecipeById(){
        // given
        String id = "4";

        when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

        // when
        recipeService.deleteById(id);

        // then
        verify(recipeReactiveRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void getIngredients(){
        final String ingredientId = "1";

        // given
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        ingredient.setDescription("Salt");

        recipe.addIngredient(ingredient);

        // when
        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        // then
        Set<Ingredient> ingredientSet = recipeService.findById("1").block().getIngredients();
        assertNotNull(ingredientSet);

        Ingredient reqIngredient = null;
        for(Ingredient i : ingredientSet){
            if (i.getId().equals(ingredientId)){
                reqIngredient = i;
                break;
            }
        }

        assertNotNull(reqIngredient);
        assertEquals(reqIngredient.getDescription(), "Salt");
    }

    @Test
    public void testGetRecipeByIdTestNotFound() throws Exception {
        // given
        when(recipeReactiveRepository.findById(anyString())).thenReturn(null);

        // when
        NotFoundException notFoundException = assertThrows(
                NotFoundException.class, () -> recipeService.findById("1"),
                "Expected exception to throw an error. But it didn't"
        );

        // then
        assertTrue(notFoundException.getMessage().contains("Recipe Not Found"));
    }
}