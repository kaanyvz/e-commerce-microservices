package com.ky.productservice.service;

import com.ky.productservice.dto.CommentDto;
import com.ky.productservice.mapper.CommentMapper;
import com.ky.productservice.model.Comment;
import com.ky.productservice.model.Product;
import com.ky.productservice.repository.CommentRepository;
import com.ky.productservice.request.create.CreateCommentRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ProductService productService;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, ProductService productService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.productService = productService;
    }

    public CommentDto createComment(CreateCommentRequest request) {
        Product product = productService.getProductById(request.getProductId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated.");
        }

        String username = authentication.getName();
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("Username is not available.");
        }

        Comment comment = Comment.commentBuilder()
                .commentCreator(username)
                .text(request.getCommentText())
                .product(product)
                .build();
        comment.setCreatedDate(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentConverter(savedComment);
    }

    public String deleteComment(Integer id){
        Comment comment = commentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Could not found"));

        commentRepository.delete(comment);
        return "Comment has successfully deleted.";
    }

}
