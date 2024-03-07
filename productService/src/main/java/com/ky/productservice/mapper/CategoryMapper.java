package com.ky.productservice.mapper;

import com.ky.productservice.dto.CategoryDto;
import com.ky.productservice.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDto categoryConverter(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
