package com.ravi.recipemongoapp.repositories.reactive;

import com.ravi.recipemongoapp.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 5/5/2020 */
public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {
    Mono<Category> findByDescription(String description);
}
