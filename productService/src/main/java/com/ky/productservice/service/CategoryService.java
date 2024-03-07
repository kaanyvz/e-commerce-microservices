package com.ky.productservice.service;

import com.ky.productservice.dto.CategoryDto;
import com.ky.productservice.mapper.CategoryMapper;
import com.ky.productservice.model.Category;
import com.ky.productservice.repository.CategoryRepository;
import com.ky.productservice.request.create.CreateCategoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDto createCategory(CreateCategoryRequest request){
        Category category = Category.builder()
                .name(request.getCategoryName())
                .build();

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.categoryConverter(savedCategory);
    }

    public List<CategoryDto> getAll(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::categoryConverter).collect(Collectors.toList());
    }

    public String deleteCategory(String categoryName){
        Category category = categoryRepository.findByName(categoryName);
        return "This category has been deleted successfully." + categoryName;
    }
}
