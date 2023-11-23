package unilearn.unilearn.studyDetailRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentsPosts;
import unilearn.unilearn.assignmentsSubmitPosts.entity.AssignmentsSubmitPosts;
import unilearn.unilearn.user.entity.User;

import java.util.List;

//과제제출게시글 레포지토리
@Repository
public interface AssignmentSubmitPostRepository extends JpaRepository<AssignmentsSubmitPosts, Long> {



    List<AssignmentsSubmitPosts> findByAssignmentsPosts(AssignmentsPosts assignmentsPosts);
    AssignmentsSubmitPosts findByAssignmentsPostsAndUser(AssignmentsPosts assignmentsPosts , User user);


}