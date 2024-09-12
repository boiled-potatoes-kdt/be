package com.dain_review.domain.user.repository;


import com.dain_review.domain.user.exception.UserException;
import com.dain_review.domain.user.exception.errortype.UserErrorCode;
import com.dain_review.domain.user.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNameAndPhone(String name, String phone);

    default User getUserById(Long id) {
        return findById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
