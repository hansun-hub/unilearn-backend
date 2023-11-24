package unilearn.unilearn.studySchedule.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unilearn.unilearn.Schedule.entity.Schedule;
import unilearn.unilearn.studySchedule.entity.StudySchedule;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
}
