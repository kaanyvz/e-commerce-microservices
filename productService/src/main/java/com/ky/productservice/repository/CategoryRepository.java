package com.ky.productservice.repository;

import com.ky.productservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String categoryName);

    Optional<Category> findByNameIgnoreCase(String categoryName);
}
