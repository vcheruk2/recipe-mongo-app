package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.converters.RecipeCommandToRecipe;
import com.ravi.recipemongoapp.converters.RecipeToRecipeCommand;
import com.ravi.recipemongoapp.domain.Ingredient;
import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.exceptions.NotFoundException;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;

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
    static RecipeRepository recipeRepository;

    @BeforeAll
    public static void setUp() throws Exception{
        MockitoAnnotations.initMocks(new RecipeServiceImplTest());
        recipeService = new RecipeServiceImpl(recipeRepository, recipeToRecipeCommand, recipeCommandToRecipe);
    }

    @Test
    @DirtiesContext
    public void getRecipes() throws Exception{
        Recipe recipe = new Recipe();
        Set<Recipe> recipeSet = new HashSet<>();
        recipeSet.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipeSet);

        assertEquals(1,
                recipeService.getRecipes().size());
        verify(recipeRepository, times(1)).findAll();
    }

    /*@Test
    public void getRecipesByIdTest() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setDescription("New Recipe");

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        assertNotNull(recipeService.findById(1L), "Null Recipe returned");
        assertEquals(recipe.getDescription(), recipeService.findById(1L).getDescription());
        verify(recipeRepository, times(2)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }*/

    @Test
    public void deleteRecipeById(){
        // given
        String id = "4";

        // when
        recipeService.deleteById(id);

        // then
        verify(recipeRepository, times(1)).deleteById(anyString());
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
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        // then
        Set<Ingredient> ingredientSet = recipeService.findById("1").getIngredients();
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
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        // when
        NotFoundException notFoundException = assertThrows(
                NotFoundException.class, () -> recipeService.findById("1"),
                "Expected exception to throw an error. But it didn't"
        );

        // then
        assertTrue(notFoundException.getMessage().contains("Recipe Not Found"));
    }
}