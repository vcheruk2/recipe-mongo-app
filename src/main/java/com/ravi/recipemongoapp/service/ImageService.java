package com.ravi.recipemongoapp.service;

import org.springframework.web.multipart.MultipartFile;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/18/2020 */
public interface ImageService {
    void saveImageFile(String recipeId, MultipartFile file);
}
