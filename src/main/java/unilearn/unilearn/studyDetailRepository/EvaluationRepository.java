package unilearn.unilearn.studyDetailRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.assignmentsSubmitPosts.entity.AssignmentsSubmitPosts;
import unilearn.unilearn.evaluation.entity.Evaluation;
import unilearn.unilearn.user.entity.User;

import java.util.Optional;

//과제 평가 게시글 레포지토리
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findByAssignmentsSubmitPostsAndUser(AssignmentsSubmitPosts assignmentsSubmitPosts, User user);

}







