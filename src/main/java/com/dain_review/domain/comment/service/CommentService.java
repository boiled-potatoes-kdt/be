package com.dain_review.domain.comment.service;

import com.dain_review.domain.comment.excepiton.CommentException;
import com.dain_review.domain.comment.excepiton.errortype.CommentErrorCode;
import com.dain_review.domain.comment.model.entity.Comment;
import com.dain_review.domain.comment.model.request.CommentRequest;
import com.dain_review.domain.comment.model.response.CommentResponse;
import com.dain_review.domain.comment.repository.CommentRepository;
import com.dain_review.domain.post.exception.PostException;
import com.dain_review.domain.post.exception.errortype.PostErrorCode;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    /**
     * 댓글 페이지네이션, 10개씩 조회 -> 조회 정령에 대해서 수정이 필요할듯?
     * @param postId
     * @return
     */
    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(Long postId) {
        Page<Comment> comments = commentRepository.findByPostId(postId, PageRequest.of(0, 10));
        return comments.map(CommentResponse::from);
    }

    /**
     * 댓글 작성 메서드
     * @param request
     */
    @Transactional
    public void createComment(CommentRequest request) {
        // 유저 정보 조회
        User user = getUserInfo(1L);
        // post, parent 객체 조회
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        //부모 댓글이 존재하는지
        Comment parent = checkCommentExistence(request.parentId());
        Comment saveComment = Comment.from(request, user, post, parent);
        Comment saved = commentRepository.save(saveComment);
        if (saved == null) {
            throw new CommentException(CommentErrorCode.COMMENT_CREATION_FAILED);
        }
    }

    // todo user -> 로그인 유저 정보로 작동하도록 수정 필요
    @Transactional
    public void updateComment(CommentRequest request) {
        User user = getUserInfo(1L);
        //해당 댓글이 존재하는지
        Comment comment = checkCommentExistence(request.id());

        // 요청 클라이언트와 댓글 작성자가 일치하는지 여부를 확인
        checkAuthorMismatch(user, comment);

        Comment updateComment = Comment.from(request, user, comment);
        Comment updated = commentRepository.save(updateComment);
        if (updated == null) {
            throw new CommentException(CommentErrorCode.COMMENT_UPDATE_FAILED);
        }
    }

    @Transactional
    public void deleteComment(CommentRequest request) {
        //해당 유처 및 댓글이 존재하는지
        User user = getUserInfo(1L);
        Comment comment = checkCommentExistence(request.id());

        // 요청 클라이언트와 댓글 작성자가 일치하는지 여부를 확인
        checkAuthorMismatch(user, comment);

        // 댓글 삭제
        commentRepository.deleteById(request.id());
    }

    private Comment checkCommentExistence(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    private User getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND)); // 테스트를 위한 임시 유저 데이터 조회
    }

    private void checkAuthorMismatch(User user, Comment comment) {
        User commentAuthor = getUserInfo(comment.getUser().getId());
        if (!commentAuthor.getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.COMMENT_AUTHOR_MISMATCH);
        }
    }
}
