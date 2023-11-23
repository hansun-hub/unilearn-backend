package unilearn.unilearn.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import unilearn.unilearn.subject.entity.Subject;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    List<Subject> findAllByOrderByIdDesc();

    @Query("SELECT s FROM Subject s ORDER BY s.id ASC")
    List<Subject> findAllASC();
}
