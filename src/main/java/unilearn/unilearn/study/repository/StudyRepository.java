package unilearn.unilearn.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.user.entity.User;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study,Long> {
    List<Study> findBySubjectAndIsOpen(Subject subject, boolean isOpen);
    List<Study> findByIsOpenFalseAndStudyStatus(String studyStatus);
    List<Study> findByUser(User user);
    List<Study> findByUserNicknameAndStudyLeaderName(String userNickname, String studyLeaderName);
    @Query("SELECT s FROM Study s WHERE s.study_id IN :studyIds")
    List<Study> findByStudyIdIn(@Param("studyIds") List<Long> studyIds);

    // 사용자가 참여한 study_id 목록을 통해 Study 목록을 조회
//    List<Study> findByStudyIdIn(List<Long> studyIds);
}
