package com.dain_review.domain.comment.service;


import com.dain_review.domain.comment.excepiton.CommentException;
import com.dain_review.domain.comment.excepiton.errortype.CommentErrorCode;
import com.dain_review.domain.comment.model.entity.Comment;
import com.dain_review.domain.comment.model.request.CommentRequest;
import com.dain_review.domain.comment.model.response.CommentResponse;
import com.dain_review.domain.comment.model.response.CommentsAndRepliesResponse;
import com.dain_review.domain.comment.repository.CommentRepository;
import com.dain_review.domain.post.event.PostCommentEvent;
import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errortype.PostErrorCode;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import com.dain_review.global.model.response.PagedResponse;
import com.dain_review.global.util.S3Util;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final S3Util s3Util;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 댓글 size 10개로 페이지네이션, 대댓글 리스트 조회
     *
     * @param postId 댓글 조회할 게시글 ID
     * @return 해당하는 페이지의 댓글 리스트 및 페이지 정보
     */
    @Transactional(readOnly = true)
    public CommentsAndRepliesResponse getComments(Long postId, int page, int size) {
        // parent 컬럼이 null 인 데이터(대댓글이 아닌 댓글)만 조회
        Page<Comment> comments =
                commentRepository.findByPostIdAndDeletedFalseAndParentIsNull(
                        postId, PageRequest.of(page - 1, size));

        List<CommentResponse> commentResponseList =
                comments.get()
                        .map(
                                comment -> {
                                    String profileUrl =
                                            s3Util.selectImage(comment.getUser().getProfileImage());
                                    return CommentResponse.from(
                                            comment, comment.getUser().getNickname(), profileUrl);
                                })
                        .toList();
        List<CommentResponse> replyList =
                findChildCommentsByCommentId(comments.get().map(Comment::getId).toList());

        PagedResponse<CommentResponse> pagedComments =
                new PagedResponse<>(
                        commentResponseList, comments.getTotalElements(), comments.getTotalPages());
        return new CommentsAndRepliesResponse(pagedComments, replyList);
    }

    /**
     * 댓글 작성(생성) 메서드
     *
     * @param request postId:작성할 댓글의 대상 게시글 ID, parentId:생성 대댓글의 부모 댓글 ID, content: 댓글 내용
     */
    @Transactional
    public void createComment(Long userId, CommentRequest request) {
        User user = getUserInfo(userId);
        Post post =
                postRepository
                        .findById(request.postId())
                        .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Comment parent = null;
        if (request.parentId() != null)
            parent =
                    commentRepository
                            .findByIdAndDeletedFalse(request.parentId())
                            .orElseThrow(
                                    () -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        Comment saveComment = Comment.from(request, user, post, parent);
        Comment saved = commentRepository.save(saveComment);
        if (saved == null) {
            throw new CommentException(CommentErrorCode.COMMENT_CREATE_FAILED);
        }
        eventPublisher.publishEvent(new PostCommentEvent(saved.getPost()));
    }

    /**
     * 댓글 수정 메서드
     *
     * @param request id:댓글 ID, postId:게시글 ID, parentId:부모 댓글 ID, content:댓글 내용
     */
    @Transactional
    public void updateComment(Long userId, CommentRequest request) {
        Comment comment = checkAuthorMismatch(userId, request.id());

        Comment updateComment = Comment.from(request, comment);
        Comment updated = commentRepository.save(updateComment);
        if (updated == null) {
            throw new CommentException(CommentErrorCode.COMMENT_UPDATE_FAILED);
        }
    }

    /**
     * 댓글 삭제 메서드, deleted 필드 값을 1로 update
     *
     * @param request id:댓글 ID
     */
    @Transactional
    public void deleteComment(Long userId, CommentRequest request) {
        Comment comment = checkAuthorMismatch(userId, request.id());

        Comment delete =
                Comment.builder()
                        .id(comment.getId())
                        .post(comment.getPost())
                        .parent(comment.getParent())
                        .children(comment.getChildren())
                        .content(comment.getContent())
                        .deleted(true)
                        .build();
        Comment deleted = commentRepository.save(delete);
        if (deleted == null) {
            throw new CommentException(CommentErrorCode.COMMENT_DELETE_FAILED);
        }
    }

    /**
     * 해당 id의 댓글의 존재 여부를 확인
     *
     * @param commentId 확인할 댓글 ID
     * @return (댓글이 존재할 시) 해당 ID의 댓글 정보 반환
     */
    private Comment checkCommentExistence(Long commentId) {
        return commentRepository
                .findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    /**
     * 요청 클라이언트의 정보를 반환
     *
     * @param userId 해당 유저의 User ID
     * @return 유저 정보 반환
     */
    private User getUserInfo(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    /**
     * 요청 클라이언트와 댓글 작성자 일치여부 확인
     *
     * @param userId    요청을 보낸 클라이언트 User ID
     * @param commentId 대상 댓글의 ID
     * @return (요청 클라이언트와 댓글 저자 일치 시) 해당 ID의 댓글 정보 반환
     */
    private Comment checkAuthorMismatch(Long userId, Long commentId) {
        Comment comment = checkCommentExistence(commentId);
        User commentAuthor = getUserInfo(comment.getUser().getId());
        if (!commentAuthor.getId().equals(userId)) {
            throw new CommentException(CommentErrorCode.COMMENT_AUTHOR_MISMATCH);
        }
        return comment;
    }

    /**
     * 대댓글 조회 메서드 (주어진 list id와 parentId가 일치하는 comment 조회)
     *
     * @param commentIds 부모 댓글 id 리스트
     * @return 대댓글 리스트
     */
    private List<CommentResponse> findChildCommentsByCommentId(List<Long> commentIds) {
        List<Comment> replies =
                commentRepository.findByParentIdInAndDeletedFalseOrderByParentId(commentIds);
        return replies.stream()
                .map(
                        comment -> {
                            String profileUrl =
                                    s3Util.selectImage(comment.getUser().getProfileImage());
                            return CommentResponse.from(
                                    comment, comment.getUser().getNickname(), profileUrl);
                        })
                .toList();
    }
}
