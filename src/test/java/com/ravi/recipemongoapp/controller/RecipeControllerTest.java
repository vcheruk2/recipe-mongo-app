package com.ravi.recipemongoapp.controller;

import com.ravi.recipemongoapp.commands.RecipeCommand;
import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.exceptions.NotFoundException;
import com.ravi.recipemongoapp.service.RecipeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/3/2020 */
public class RecipeControllerTest {

    static RecipeController recipeController;
    static MockMvc mockMvc;

    @Mock
    static RecipeService recipeService;

    @BeforeAll
    public static void setUp(){
        MockitoAnnotations.initMocks(new RecipeControllerTest());
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void testGetNewRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(String.valueOf(2L));

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string")
                .param("directions", "Cooking directions")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    public void testPostNewRecipeFormValidationFailure() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(String.valueOf(2L));

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                //.param("description", "some string")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"));
    }

    @Test
    public void testGetUpdateView() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(String.valueOf(2L));

        when(recipeService.findCommandById(anyString())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(get("/recipe/3/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyString());
    }

    @Test
    public void testGetRecipeNotFound() throws Exception {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(String.valueOf(2L));

        // when
        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Disabled("Converted id to String.")
    @Test
    public void testNumberFormatException() throws Exception {

        // given
        //when(recipeService.findById(any())).thenThrow(NumberFormatException.class);

        // when
        mockMvc.perform(get("/recipe/shouldBeNumber/show"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    /*@Test
    public void testGetRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription("Test recipe");
        recipe.setId(1L);

        when(recipeService.findById(anyString())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findById(anyString());
    }*/
}
