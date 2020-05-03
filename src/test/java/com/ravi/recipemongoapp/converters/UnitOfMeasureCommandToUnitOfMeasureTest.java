package com.ravi.recipemongoapp.converters;

import com.ravi.recipemongoapp.commands.UnitOfMeasureCommand;
import com.ravi.recipemongoapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {

    public UnitOfMeasureCommandToUnitOfMeasure converter;
    public static final String ID = "1";
    public static final String DESCRIPTION = "UOMCommand Description";

    @BeforeEach
    void setUp() throws Exception{
        converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    void testNullParameter() throws Exception{
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() throws Exception{
        assertNotNull(converter.convert(new UnitOfMeasureCommand()));
    }

    @Test
    void convert() throws Exception{
        // given
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(ID);
        unitOfMeasureCommand.setDescription(DESCRIPTION);

        // when
        UnitOfMeasure unitOfMeasure = converter.convert(unitOfMeasureCommand);

        // then
        assertNotNull(unitOfMeasure);
        assertEquals(unitOfMeasure.getDescription(), DESCRIPTION);
        assertEquals(unitOfMeasure.getId(), ID);
    }
}