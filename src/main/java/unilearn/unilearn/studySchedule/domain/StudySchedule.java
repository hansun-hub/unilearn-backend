package unilearn.unilearn.studySchedule.domain;

import lombok.*;
import unilearn.unilearn.study.entity.Study;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="studyschedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudySchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="study_schedule_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id")
    private Study study;


    /*@OneToMany(mappedBy = "user")
    private List<User> studyScheduleUsers;*/

    private int year;
    private int month;
    private String date;
    private String scheduleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ElementCollection
    private List<Integer> scheduleCount;

}
