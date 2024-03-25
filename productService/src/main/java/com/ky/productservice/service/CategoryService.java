package com.ky.productservice.service;

import com.ky.productservice.dto.CategoryDto;
import com.ky.productservice.exc.CategoryNameNotProvidedException;
import com.ky.productservice.exc.CategoryNotFoundException;
import com.ky.productservice.mapper.CategoryMapper;
import com.ky.productservice.model.Category;
import com.ky.productservice.repository.CategoryRepository;
import com.ky.productservice.request.create.CreateCategoryRequest;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category getCategoryById(Integer id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Error"));
    }

    public CategoryDto createCategory(CreateCategoryRequest request) {
        if (request == null || StringUtils.isBlank(request.getCategoryName())) {
            throw new CategoryNameNotProvidedException("Category name is required.");
        }

        String categoryName = request.getCategoryName().toLowerCase();

        // Check if the entered category already exists (case-insensitive)
        Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(categoryName);
        if (existingCategory.isPresent()) {
            throw new CategoryNameNotProvidedException("Category '" + categoryName + "' already exists.");
        }

        Category category = Category.builder()
                .name(categoryName)
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
        categoryRepository.delete(category);
        return "This category has been deleted successfully." + categoryName;
    }
}
