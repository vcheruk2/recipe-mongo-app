package com.ravi.recipemongoapp.controller;

import com.ravi.recipemongoapp.converters.RecipeCommandToRecipe;
import com.ravi.recipemongoapp.converters.RecipeToRecipeCommand;
import com.ravi.recipemongoapp.domain.Category;
import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import com.ravi.recipemongoapp.repositories.CategoryRepository;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import com.ravi.recipemongoapp.repositories.UnitOfMeasureRepository;
import com.ravi.recipemongoapp.service.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {

    public static IndexController indexController;
    public static RecipeServiceImpl recipeService;

    @Mock
    static RecipeCommandToRecipe recipeCommandToRecipe;
    @Mock
    static RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    public static CategoryRepository categoryRepository;
    @Mock
    public static UnitOfMeasureRepository unitOfMeasureRepository;
    @Mock
    public static RecipeRepository recipeRepository;
    @Mock
    public static Model model;

    @BeforeAll
    public static void setUp(){
        MockitoAnnotations.initMocks(new IndexControllerTest());
        recipeService = new RecipeServiceImpl(recipeRepository, recipeToRecipeCommand, recipeCommandToRecipe);
        indexController = new IndexController(categoryRepository, unitOfMeasureRepository, recipeRepository, recipeService);
    }

    @Test
    void testMockMvc() throws Exception {
        Category category = new Category();
        category.setId(String.valueOf(1));
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(String.valueOf(1));

        when(categoryRepository.findByDescription("American")).thenReturn(Optional.of(category));
        when(unitOfMeasureRepository.findByDescription("Teaspoon")).thenReturn(Optional.of(unitOfMeasure));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"));
    }

    @Test
    void mapIndex() {
        Set<Recipe> recipes = new HashSet<>();
        Recipe recipe = new Recipe();
        recipe.setDescription("recipe");
        Recipe recipe2 = new Recipe();
        recipe2.setDescription("recipe2");

        recipes.add(recipe);
        recipes.add(recipe2);

        //log.debug("recipes size = "+recipes.size());

        Category category = new Category();
        category.setId(String.valueOf(1));
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(String.valueOf(1));

        when(categoryRepository.findByDescription("American")).thenReturn(Optional.of(category));
        when(unitOfMeasureRepository.findByDescription("Teaspoon")).thenReturn(Optional.of(unitOfMeasure));

        assertEquals("1", categoryRepository.findByDescription("American").get().getId());
        verify(categoryRepository, times(1)).findByDescription("American");

        when(recipeRepository.findAll()).thenReturn(recipes);
        assertEquals(2, recipeService.getRecipes().size());
        verify(recipeRepository, times(1)).findAll();

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        assertEquals("index", indexController.mapIndex(model));
        //verify(model, times(1)).addAttribute(eq("recipes"), anySet());
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> capturedRecipes = argumentCaptor.getValue();
        assertEquals(2, capturedRecipes.size());
    }


}