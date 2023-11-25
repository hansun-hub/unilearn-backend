package unilearn.unilearn.Schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unilearn.unilearn.Schedule.entity.Schedule;
import unilearn.unilearn.user.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserAndDeadline(User user, LocalDate deadline);

    @Query("SELECT s FROM Schedule s WHERE YEAR(s.deadline) = :year AND MONTH(s.deadline) = :month AND s.user= :user")
    List<Schedule> findByYearAndMonth(@Param("year") int year, @Param("month") int month, @Param("user") User user);
}
