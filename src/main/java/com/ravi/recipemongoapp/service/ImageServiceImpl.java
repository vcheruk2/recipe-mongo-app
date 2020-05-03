package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/18/2020 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(String recipeId, MultipartFile file) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        // TODO: Add more validation logic here
        if (!recipeOptional.isPresent()){
            log.error("Unable to find recipe for id " + recipeId);
            return;
        }

        Recipe recipe = recipeOptional.get();

        try {
            Byte[] bytesObj = new Byte[file.getBytes().length];

            int idx = 0;
            for (byte b : file.getBytes())
                bytesObj[idx++] = b;

            recipe.setImage(bytesObj);
            recipeRepository.save(recipe);

        } catch (IOException e) {
            // TODO: Handle exception
            e.printStackTrace();
        }
    }
}
