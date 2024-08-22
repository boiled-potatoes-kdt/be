package com.dain_review.domain.comment.service;

import com.dain_review.domain.comment.excepiton.CommentException;
import com.dain_review.domain.comment.excepiton.errortype.CommentErrorCode;
import com.dain_review.domain.comment.model.entity.Comment;
import com.dain_review.domain.comment.model.request.CommentRequest;
import com.dain_review.domain.comment.model.response.CommentResponse;
import com.dain_review.domain.comment.repository.CommentRepository;
import com.dain_review.domain.post.model.entity.Post;
import com.dain_review.domain.post.repository.PostRepository;
import com.dain_review.domain.user.model.entity.User;
import com.dain_review.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public void createComment(CommentRequest request) {
        //
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다.")); // 테스트를 위한 임시 유저 데이터 조회
        // post, parent 객체 조회
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        Comment parent = commentRepository.findById(request.parentId())
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        Comment saveComment = Comment.from(request, user, post, parent);
        Comment saved = commentRepository.save(saveComment);
        if (saved == null) {
            throw new RuntimeException("댓글 생성 실패!");
        }
    }

    // todo user -> 로그인 유저 정보로 작동하도록 수정 필요
    public void updateComment(CommentRequest request) {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다.")); // 테스트를 위한 임시 유저 데이터 조회
        // post, parent 객체 조회
        Comment comment = commentRepository.findById(request.id())
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        // 요청을 보낸 클라이언트의 userId와 comment의 userId가 일치하지 않으면 예외를 더뜨림
        User author = userRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        if (!author.getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.COMMENT_AUTHOR_MISMATCH);
        }

        Comment updateComment = Comment.from(request, user, comment);
        Comment updated = commentRepository.save(updateComment);
        if (updated == null) {
            throw new CommentException(CommentErrorCode.COMMENT_UPDATE_FAILED);
        }
    }

    public void deleteComment(CommentRequest request) {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다.")); // 테스트를 위한 임시 유저 데이터 조회

        //해당 게시글이 존재하는지
        Comment comment = findOneComment(request);

        User author = userRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        if (!author.getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.COMMENT_AUTHOR_MISMATCH);
        }

        // 댓글 삭제
        commentRepository.deleteById(request.id());
    }

    private Comment findOneComment(CommentRequest request) {
        return commentRepository.findById(request.id())
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
