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
        List<CommentDto> comments,
        LocalDateTime createdDate
) {
}
