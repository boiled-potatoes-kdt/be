package com.dain_review.domain.comment.model.response;


import com.dain_review.global.model.response.PagedResponse;
import java.util.List;

public record CommentsAndRepliesResponse(
        PagedResponse<CommentResponse> comments, List<CommentResponse> replies) {}
