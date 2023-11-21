package unilearn.unilearn.assignmentsPosts.entity;

import lombok.*;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="assignmentsposts")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class AssignmentsPosts {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="assignments_posts_id")
    private Long id;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id")
    private Study study;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    private String status;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deadline;



}
