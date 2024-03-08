package com.ky.productservice.controller;

import com.ky.productservice.dto.CommentDto;
import com.ky.productservice.request.create.CreateCommentRequest;
import com.ky.productservice.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/addComment")
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentRequest request){
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer id){
        return ResponseEntity.ok(commentService.deleteComment(id));
    }

}
