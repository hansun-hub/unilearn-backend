package unilearn.unilearn.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unilearn.unilearn.subject.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
