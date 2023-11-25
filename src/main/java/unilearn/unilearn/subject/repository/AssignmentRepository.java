package unilearn.unilearn.subject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unilearn.unilearn.subject.entity.Assignment;

public interface AssignmentRepository  extends JpaRepository<Assignment, Long> {
}
