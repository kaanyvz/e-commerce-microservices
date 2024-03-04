package com.ky.productservice.request.create;

import lombok.Getter;

@Getter
public class CreateCommentRequest {
    private Integer productId;
    private String commentText;
}
