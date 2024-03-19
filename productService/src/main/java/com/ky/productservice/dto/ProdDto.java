package com.ky.productservice.dto;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProdDto(
        Integer id,
        String name,
        CategoryDto categoryDto,
        BigDecimal unitPrice,
        String desc,
        String imageUrl,
        Integer stockCount,
        List<CommentDto> comments,
        LocalDateTime createdDate
) {
}
