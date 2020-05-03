package com.ravi.recipemongoapp.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/4/2020 */
@Getter
@Setter
@NoArgsConstructor
public class NotesCommand {

    private String id;
    private String recipeNotes;
}
