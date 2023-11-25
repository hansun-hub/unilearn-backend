package unilearn.unilearn.studySchedule.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unilearn.unilearn.Schedule.entity.Schedule;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.studySchedule.entity.StudySchedule;
import unilearn.unilearn.user.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
    @Query("SELECT s FROM StudySchedule s WHERE YEAR(s.deadline) = :year AND MONTH(s.deadline) = :month AND s.user= :user AND s.study= :study")
    List<StudySchedule> findByYearAndMonth(@Param("year") int year, @Param("month") int month, @Param("user") User user, @Param("study") Study study);

    List<StudySchedule> findByUserAndDeadlineAndStudy(User user, LocalDate deadline, Study study);
}