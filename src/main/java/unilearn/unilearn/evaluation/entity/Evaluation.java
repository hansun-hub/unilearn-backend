package unilearn.unilearn.evaluation.entity;

import lombok.*;
import unilearn.unilearn.assignmentsSubmitPosts.entity.AssignmentsSubmitPosts;
import unilearn.unilearn.user.entity.User;

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
    @JoinColumn(name="assignments_submit_posts")

    private AssignmentsSubmitPosts assignmentsSubmitPosts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String feedback;

    //클라이언트에게 설문 1~5 점수 밭아옴
    private int survey1;
    private int survey2;
    private int survey3;
    private int survey4;
    private int survey5;
    //과제 제출 목록에서 피드백 확인할 때 총점으로 보여주므로 total 점수 추가 - 서윤
    private int total_score;


    private LocalDateTime createdAt;

}
