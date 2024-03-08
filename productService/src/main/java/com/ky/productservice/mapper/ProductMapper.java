package com.ky.productservice.mapper;

import com.ky.productservice.dto.ProdDto;
import com.ky.productservice.model.Product;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {
    private final CommentMapper commentMapper;
    private final CategoryMapper categoryMapper;

    public ProductMapper(CommentMapper commentMapper, CategoryMapper categoryMapper) {
        this.commentMapper = commentMapper;
        this.categoryMapper = categoryMapper;
    }

    public ProdDto productConverter(Product product){
        return ProdDto.builder()
                .id(product.getId())
                .name(product.getName())
                .unitPrice(product.getUnitPrice())
                .createdDate(product.getCreatedDate())
                .desc(product.getDescription())
                .imageUrl(product.getImageUrl())
                .comments(product.getComments().stream().map(commentMapper::commentConverter).collect(Collectors.toList()))
                .categoryDto(categoryMapper.categoryConverter(product.getCategory()))
                .build();
    }
}
