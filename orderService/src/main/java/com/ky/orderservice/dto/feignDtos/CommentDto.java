package com.ky.orderservice.dto.feignDtos;

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
