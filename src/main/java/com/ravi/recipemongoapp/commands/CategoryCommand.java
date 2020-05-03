package com.ravi.recipemongoapp.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/4/2020 */
@Setter
@Getter
@NoArgsConstructor
public class CategoryCommand {
    private String id;
    private String description;
}
