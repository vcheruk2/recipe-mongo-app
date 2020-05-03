package com.ravi.recipemongoapp.controller;

import com.ravi.recipemongoapp.commands.RecipeCommand;
import com.ravi.recipemongoapp.exceptions.NotFoundException;
import com.ravi.recipemongoapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/4/2020 */
@Controller
@Slf4j
public class RecipeController {

    RecipeService recipeService;
    private static final String VIEW_RECIPE_FORM = "recipe/recipeform";

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"recipe/{id}/show"})
    public String getRecipeById(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/show";
    }

    @GetMapping({"recipe/new"})
    public String recipeForm(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return VIEW_RECIPE_FORM;
    }

    @PostMapping("recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult result){

        if (result.hasErrors()){
            result.getAllErrors().forEach( objectError -> {
                log.debug(objectError.toString());
            });

            return VIEW_RECIPE_FORM;
        }
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return VIEW_RECIPE_FORM;
    }

    @GetMapping("recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id){
        log.debug("Deleting id: " + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception){
        log.debug("Handling not found exception");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }
}
