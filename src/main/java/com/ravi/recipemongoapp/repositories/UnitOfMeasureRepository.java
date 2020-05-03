package com.ravi.recipemongoapp.repositories;

import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 3/29/2020 */
public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

    Optional<UnitOfMeasure> findByDescription(String description);
}
