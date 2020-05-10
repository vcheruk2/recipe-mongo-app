package com.ravi.recipemongoapp.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/18/2020 */
public interface ImageService {
    Mono<Void> saveImageFile(String recipeId, MultipartFile file);
}
