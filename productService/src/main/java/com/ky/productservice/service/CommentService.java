package com.ky.productservice.service;

import com.ky.commons.model.UserCredential;
import com.ky.productservice.dto.CommentDto;
import com.ky.productservice.mapper.CommentMapper;
import com.ky.productservice.model.Comment;
import com.ky.productservice.model.Product;
import com.ky.productservice.repository.CommentRepository;
import com.ky.productservice.request.create.CreateCommentRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public CommentDto createComment(CreateCommentRequest request){
        Product product = productService.getProductById(request.getProductId());
        UserCredential userCredential =(UserCredential) SecurityContextHolder.getContext()
                .getAuthentication().getCredentials();

        Comment comment = Comment.builder()
                .commentCreator(userCredential.getUsername())
                .text(request.getCommentText())
                .product(product)
                .build();
        Comment savedCom = commentRepository.save(comment);
        return commentMapper.commentConverter(savedCom);
    }

    public String deleteComment(Integer id){
        Comment comment = commentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Could not found"));

        commentRepository.delete(comment);
        return "Comment has successfully deleted.";
    }

}
