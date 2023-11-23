package unilearn.unilearn.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.user.entity.MySubject;
import unilearn.unilearn.user.entity.User;

import java.util.List;

@Repository
public interface MySubjectRepository extends JpaRepository<MySubject, Long> {
    List<MySubject> findAllByUser(User user);
}
