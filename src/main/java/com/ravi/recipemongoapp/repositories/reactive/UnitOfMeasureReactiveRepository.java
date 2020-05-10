package com.ravi.recipemongoapp.repositories.reactive;

import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 5/5/2020 */
@Repository
public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {
    Mono<UnitOfMeasure> findByDescription(String description);
}
