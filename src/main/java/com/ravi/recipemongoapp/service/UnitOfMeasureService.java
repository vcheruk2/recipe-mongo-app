package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.UnitOfMeasureCommand;

import java.util.Set;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/12/2020 */
public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUoms();
}
