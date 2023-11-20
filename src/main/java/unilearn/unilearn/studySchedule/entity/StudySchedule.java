package unilearn.unilearn.studySchedule.entity;

import lombok.*;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id")
    private Study study;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

//    private int year;
//    private int month;
//    private String date;
    private LocalDateTime scheduleDate;
    private String scheduleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    @ElementCollection
//    private List<Integer> scheduleCount;

}
