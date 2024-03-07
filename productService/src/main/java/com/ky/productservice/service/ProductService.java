package com.ky.productservice.service;

import com.ky.productservice.dto.ProdDto;
import com.ky.productservice.mapper.ProductMapper;
import com.ky.productservice.model.Product;
import com.ky.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }







    public Product getProductById(Integer id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found."));
    }
}
