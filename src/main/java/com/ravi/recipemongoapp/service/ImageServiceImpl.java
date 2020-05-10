package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.domain.Recipe;
import com.ravi.recipemongoapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/18/2020 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {
        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    Byte[] byteObj = new Byte[0];
                    try{
                        byteObj = new Byte[file.getBytes().length];

                        int i = 0;
                        for(byte b : file.getBytes())
                            byteObj[i++] = b;

                        recipe.setImage(byteObj);

                        return recipe;
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });

        recipeReactiveRepository.save(recipeMono.block()).block();

        return Mono.empty();
    }
}
