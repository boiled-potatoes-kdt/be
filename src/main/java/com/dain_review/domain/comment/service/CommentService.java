package com.dain_review.domain.comment.service;

import com.dain_review.domain.comment.excepiton.CommentException;
import com.dain_review.domain.comment.excepiton.errortype.CommentErrorCode;
import com.dain_review.domain.comment.model.entity.Comment;
import com.dain_review.domain.comment.model.request.CommentRequest;
import com.dain_review.domain.comment.model.response.CommentAllTypeResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 페이지네이션, 10개 댓글 리스트 조회
     * @param postId    댓글 조회할 게시글 ID
     * @return          해당하는 페이지의 댓글 리스트 및 페이지 정보
     */
    @Transactional(readOnly = true)
    public CommentAllTypeResponse getComments(Long postId, int page) {
        // parent 컬럼이 null 인 데이터(대댓글이 아닌 댓글)만 조회
        Page<Comment> comments = commentRepository
                .findByPostIdAndDeletedFalseAndParentIsNull(postId, PageRequest.of(page-1, 10));
        List<CommentResponse> replyList = findChildCommentsByCommentId(comments.get().map(Comment::getId).toList());
        Page<CommentResponse> parents = comments.map(CommentResponse::from);
        return new CommentAllTypeResponse(parents, replyList);
    }

    /**
     * 댓글 작성(생성) 메서드
     * @param request   postId:작성할 댓글의 대상 게시글 ID, parentId:생성 대댓글의 부모 댓글 ID, content: 댓글 내용
     */
    @Transactional
    public void createComment(Long userId, CommentRequest request) {
        User user = getUserInfo(userId); // 테스트를 위한 임시 유저 데이터 조회
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Comment parent = null;
        if (request.parentId()!=null)
            parent = commentRepository.findByIdAndDeletedFalse(request.parentId())
                    .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        Comment saveComment = Comment.from(request, user, post, parent);
        Comment saved = commentRepository.save(saveComment);
        if (saved == null) {
            throw new CommentException(CommentErrorCode.COMMENT_CREATE_FAILED);
        }
    }

    // todo user -> 로그인 유저 정보로 작동하도록 수정 필요
    /**
     * 댓글 수정 메서드
     * @param request   id:댓글 ID, postId:게시글 ID, parentId:부모 댓글 ID, content:댓글 내용
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
     * @param request   id:댓글 ID
     */
    @Transactional
    public void deleteComment(Long userId, CommentRequest request) {
        Comment comment = checkAuthorMismatch(userId, request.id()); // 테스트를 위한 임시 유저 데이터 조회

        Comment delete = Comment.builder()
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
     * @param commentId 확인할 댓글 ID
     * @return          (댓글이 존재할 시) 해당 ID의 댓글 정보 반환
     */
    private Comment checkCommentExistence(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    /**
     * 요청 클라이언트의 정보를 반환
     * @param userId    해당 유저의 User ID
     * @return          유저 정보 반환
     */
    private User getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    /**
     * 요청 클라이언트와 댓글 작성자 일치여부 확인
     * @param userId        요청을 보낸 클라이언트 User ID
     * @param commentId     대상 댓글의 ID
     * @return              (요청 클라이언트와 댓글 저자 일치 시) 해당 ID의 댓글 정보 반환
     */
    private Comment checkAuthorMismatch(Long userId, Long commentId) {
        Comment comment = checkCommentExistence(commentId);
        User commentAuthor = getUserInfo(comment.getUser().getId());
        if (!commentAuthor.getId().equals(userId)) {
            throw new CommentException(CommentErrorCode.COMMENT_AUTHOR_MISMATCH);
        }
        return comment;
    }

    // todo 대댓글 찾는 로직 추가
    private List<CommentResponse> findChildCommentsByCommentId(List<Long> commentIds) {
        // 주어진 list의 id와 parent가 일치하는 comment의 레코드 리스트 가져오기
        List<Comment> replies = commentRepository.findByParentIdInAndDeletedFalseOrderByParentId(commentIds);
        return replies.stream().map(CommentResponse::from).toList();
    }
}
