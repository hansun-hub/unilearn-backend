package unilearn.unilearn.Schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.Schedule.entity.Schedule;
import unilearn.unilearn.user.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserAndDeadline(User user, LocalDate deadline);
}
