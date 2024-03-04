package com.ky.productservice.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record CommentDto(
        Long id,
        String createdBy,
        LocalDateTime createdDate,
        String text,
        String creator
) {
}
