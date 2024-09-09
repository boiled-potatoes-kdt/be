package com.dain_review.domain.comment.controller;


import com.dain_review.domain.comment.model.request.CommentRequest;
import com.dain_review.domain.comment.model.response.CommentsAndRepliesResponse;
import com.dain_review.domain.comment.service.CommentService;
import com.dain_review.domain.user.config.model.CustomUserDetails;
import com.dain_review.global.api.API;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> getComments(
            @RequestParam(value = "post_id") Long postId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        CommentsAndRepliesResponse response = commentService.getComments(postId, page, size);
        return API.OK(response);
    }

    @PostMapping
    public ResponseEntity<?> createComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CommentRequest request) {
        commentService.createComment(customUserDetails.getUserId(), request);
        return API.OK();
    }

    @PatchMapping
    public ResponseEntity<?> updateComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CommentRequest request) {
        commentService.updateComment(customUserDetails.getUserId(), request);
        return API.OK();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CommentRequest request) {
        commentService.deleteComment(customUserDetails.getUserId(), request);
        return API.OK();
    }
}
