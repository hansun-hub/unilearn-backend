package unilearn.unilearn.evaluation.domain;

import lombok.*;
import unilearn.unilearn.assignmentsSubmitPosts.domain.AssignmentsSubmitPosts;
import unilearn.unilearn.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="evaluation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="evaluation_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assignments_submit_posts_id")
    private AssignmentsSubmitPosts assignmentsSubmitPosts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String feedback;
    private int survey1;
    private int survey2;
    private int survey3;
    private int survey4;
    private int survey5;

    private LocalDateTime createdAt;

}
