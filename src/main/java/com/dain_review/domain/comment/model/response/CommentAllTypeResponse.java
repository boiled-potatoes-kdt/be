package com.dain_review.domain.comment.model.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record CommentAllTypeResponse(
        Page<CommentResponse> parents,
        List<CommentResponse> replies
) { }
