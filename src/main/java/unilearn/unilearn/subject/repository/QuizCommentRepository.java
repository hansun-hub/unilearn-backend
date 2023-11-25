package unilearn.unilearn.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import unilearn.unilearn.subject.entity.QuizComment;

import java.util.List;

public interface QuizCommentRepository extends JpaRepository<QuizComment, Long> {

    @Query(value="SELECT * FROM quizcomment WHERE quiz_id= :quizId", nativeQuery = true)
    List<QuizComment> findByQuizId(Long quizId);
}
