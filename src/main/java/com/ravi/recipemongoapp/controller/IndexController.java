package com.ravi.recipemongoapp.controller;

import com.ravi.recipemongoapp.domain.Category;
import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import com.ravi.recipemongoapp.repositories.CategoryRepository;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import com.ravi.recipemongoapp.repositories.UnitOfMeasureRepository;
import com.ravi.recipemongoapp.repositories.reactive.RecipeReactiveRepository;
import com.ravi.recipemongoapp.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@Slf4j
public class IndexController {

    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;
    private RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeService recipeService;

    public IndexController(CategoryRepository categoryRepository,
                           UnitOfMeasureRepository unitOfMeasureRepository,
                           RecipeReactiveRepository recipeReactiveRepository,
                           RecipeService recipeService) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeService = recipeService;
    }

    @RequestMapping({"/", "", "/index", "/index.html"})
    public String mapIndex(Model model){

        Optional<Category> optionalCategory = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> optionalUnitOfMeasure = unitOfMeasureRepository.findByDescription("Teaspoon");
        //Iterable<Recipe> recipes = recipeRepository.findAll();

        System.out.println("Category ID = "+optionalCategory.get().getId());
        System.out.println("Unit of Measure ID = "+optionalUnitOfMeasure.get().getId());

        System.out.println("hi 123");
        log.debug("Calling index page");

        //model.addAttribute("recipes", recipeRepository.findAll());
        model.addAttribute("recipes", recipeService.getRecipes().collectList().block());

        return "index";
    }
}
