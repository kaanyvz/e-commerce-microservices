package com.ky.productservice.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record CommentDto(
        Integer id,
        LocalDateTime createdDate,
        String text,
        String creator
) {
}
