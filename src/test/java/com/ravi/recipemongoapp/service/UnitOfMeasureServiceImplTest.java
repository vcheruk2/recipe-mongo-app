package com.ravi.recipemongoapp.service;

import com.ravi.recipemongoapp.commands.UnitOfMeasureCommand;
import com.ravi.recipemongoapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import com.ravi.recipemongoapp.repositories.UnitOfMeasureRepository;
import com.ravi.recipemongoapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    UnitOfMeasureServiceImpl unitOfMeasureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureReactiveRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUoms() {
        // Given
        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setId("1");
        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure2.setId("2");
        unitOfMeasures.add(unitOfMeasure1);
        unitOfMeasures.add(unitOfMeasure2);

        when(unitOfMeasureReactiveRepository.findAll()).thenReturn(Flux.just(unitOfMeasure1, unitOfMeasure2));

        // when
        Flux<UnitOfMeasureCommand> unitOfMeasureCommands = unitOfMeasureService.listAllUoms();

        // then
        assertNotNull(unitOfMeasureCommands);
        assertEquals(2, unitOfMeasureCommands.collectList().block().size());
    }
}