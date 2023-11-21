package unilearn.unilearn.Schedule.entity;

import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentsPosts;
import unilearn.unilearn.global.entity.BaseTimeEntity;
import unilearn.unilearn.studySchedule.entity.StudySchedule;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Transactional
public class Schedule extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="schedule_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_schedule_id")
    private StudySchedule studySchedule;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assignments_posts_id")
    private AssignmentsPosts assignmentsPosts;
}
