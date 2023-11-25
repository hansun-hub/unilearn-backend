package unilearn.unilearn.studyDetailRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentsPosts;
import unilearn.unilearn.study.entity.Study;

import java.util.List;

//과제 게시글 레포지토리
@Repository
public interface AssignmentPostRepository extends JpaRepository<AssignmentsPosts, Long> {
    //과제 게시글 전체 조회용 (마감전&후)
    List<AssignmentsPosts> findByStatusAndStudy(String status, Study study);

}
