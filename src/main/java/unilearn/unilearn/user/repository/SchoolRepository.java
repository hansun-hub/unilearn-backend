package unilearn.unilearn.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.user.domain.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    School findBySchoolName(String schoolName);
}
