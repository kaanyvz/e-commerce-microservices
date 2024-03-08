package com.ky.orderservice.dto;


import java.util.List;

public record Pagination<T>(
        List<T> data,
        Integer totalSize

) {
}
