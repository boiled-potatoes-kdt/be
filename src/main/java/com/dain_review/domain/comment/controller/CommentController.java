package com.dain_review.domain.comment.controller;

import com.dain_review.domain.comment.model.request.CommentRequest;
import com.dain_review.domain.comment.model.response.CommentResponse;
import com.dain_review.domain.comment.service.CommentService;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity getComments(
            @RequestParam(value="post_id") Long postId
    ) {
        Page<CommentResponse> response = commentService.getComments(postId);
        return API.OK(response);
    }

    @PostMapping
    public ResponseEntity createComment(
            @RequestBody CommentRequest request
//            @AuthenticationPrincipal  추후 구현
    ) {
        commentService.createComment(request);
        return API.OK();
    }

    @PatchMapping
    public ResponseEntity updateComment(
            @RequestBody CommentRequest request
//            @AuthenticationPrincipal  추후 구현
    ) {
        commentService.updateComment(request);
        return API.OK();
    }

    @DeleteMapping
    public ResponseEntity deleteComment(
            @RequestBody CommentRequest request
//            @AuthenticationPrincipal  추후 구현
    ) {
        commentService.deleteComment(request);
        return API.OK();
    }
}
