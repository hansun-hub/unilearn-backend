package unilearn.unilearn.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginId(String loginId);
    User findByNickname(String nickname);
    User findByEmail(String email);
}
