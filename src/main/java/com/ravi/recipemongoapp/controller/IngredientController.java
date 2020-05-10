package com.ravi.recipemongoapp.controller;

import com.ravi.recipemongoapp.commands.IngredientCommand;
import com.ravi.recipemongoapp.commands.RecipeCommand;
import com.ravi.recipemongoapp.commands.UnitOfMeasureCommand;
import com.ravi.recipemongoapp.service.IngredientService;
import com.ravi.recipemongoapp.service.RecipeService;
import com.ravi.recipemongoapp.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/7/2020 */
@Controller
@Slf4j
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping({"/recipe/{id}/ingredients"})
    public String getIngredients(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping({"/recipe/{recipe_id}/ingredient/{ingredient_id}/show"})
    public String showIngredients(@PathVariable String recipe_id,
                                  @PathVariable String ingredient_id,
                                  Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipe_id,
                                                                                ingredient_id).block());
        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping({"/recipe/{recipe_id}/ingredient/{ingredient_id}/update"})
    public String updateIngredients(@PathVariable String recipe_id,
                                    @PathVariable String ingredient_id,
                                    Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipe_id,
                                                                                                ingredient_id).block());
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList().block());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

        log.debug("saved receipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model){

        // TODO: Validation pending to see if the recipeId value is correct/acceptable.
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);

        model.addAttribute("ingredient" ,ingredientCommand);

        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList().block());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId,
                                   @PathVariable String ingredientId){
        // TODO: Validate the recipe id & ingredient id
        ingredientService.deleteById(recipeId, ingredientId);

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
