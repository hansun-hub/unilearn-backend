package unilearn.unilearn.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginId(String loginId);
    User findByNickname(String nickname);
    User findByEmail(String email);


    Long findUserIdByNickname(String userNickname);

    User getUserById(Long userId);
}
