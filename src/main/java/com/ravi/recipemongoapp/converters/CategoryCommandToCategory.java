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
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {

    @Synchronized
    @Override
    @Nullable
    public Category convert(CategoryCommand source) {
        if (source == null)
            return null;

        final Category category = new Category();
        category.setId(source.getId());
        category.setDescription(source.getDescription());
        return category;
    }
}
