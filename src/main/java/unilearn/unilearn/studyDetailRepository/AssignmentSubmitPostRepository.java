package unilearn.unilearn.studyDetailRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.assignmentsPosts.domain.AssignmentsPosts;
import unilearn.unilearn.assignmentsSubmitPosts.domain.AssignmentsSubmitPosts;

import java.time.LocalDateTime;
import java.util.List;

//과제제출게시글 레포지토리
@Repository
public interface AssignmentSubmitPostRepository extends JpaRepository<AssignmentsSubmitPosts, Long> {



    List<AssignmentsSubmitPosts> findByAssignmentsPosts(AssignmentsPosts assignmentsPosts);


}