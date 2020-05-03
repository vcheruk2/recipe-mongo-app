package com.ravi.recipemongoapp.domain;

import lombok.Getter;
import lombok.Setter;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 3/28/2020 */
@Getter
@Setter
public class Notes {

    private String id;
    private Recipe recipe;
    private String recipeNotes;

}
