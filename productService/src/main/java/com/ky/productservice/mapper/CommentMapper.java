package com.ky.productservice.mapper;

import com.ky.productservice.dto.CommentDto;
import com.ky.productservice.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentDto commentConverter(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdDate(comment.getCreatedDate())
                .creator(comment.getCommentCreator())
                .build();
    }
}
