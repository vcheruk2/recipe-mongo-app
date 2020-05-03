package com.ravi.recipemongoapp.converters;

import com.ravi.recipemongoapp.commands.CategoryCommand;
import com.ravi.recipemongoapp.domain.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/* Created by: Venkata Ravichandra Cherukuri
   Created on: 4/4/2020 */
@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {

    @Synchronized
    @Override
    @Nullable
    public CategoryCommand convert(Category source) {
        if(source == null)
            return null;

        final CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setDescription(source.getDescription());
        categoryCommand.setId(source.getId());
        return categoryCommand;
    }
}
