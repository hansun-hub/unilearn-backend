package unilearn.unilearn.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.subject.entity.Subject;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study,Long> {
    List<Study> findBySubjectAndIsOpen(Subject subject, boolean isOpen);
    List<Study> findByIsOpenFalseAndStudyStatus(String studyStatus);

}
