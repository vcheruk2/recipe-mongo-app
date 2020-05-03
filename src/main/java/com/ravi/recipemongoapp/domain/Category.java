package com.ravi.recipemongoapp.domain;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 3/29/2020 */

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Category {
    private String id;
    private String description;
    private Set<Recipe> recipes = new HashSet<>();
}
