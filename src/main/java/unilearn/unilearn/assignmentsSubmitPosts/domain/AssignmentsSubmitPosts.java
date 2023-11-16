package unilearn.unilearn.assignmentsSubmitPosts.domain;

import lombok.*;
import unilearn.unilearn.assignmentsSubmitPosts.constant.SubStatus;
import unilearn.unilearn.user.domain.School;
import unilearn.unilearn.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="assignmentssubmitposts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssignmentsSubmitPosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="assignments_submit_posts_id")
    private Long id;

    /*
    @ManyToOne
    @JoinColumn(name="study_id")
    private Study study;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    private String status;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long evaluation;

    // enum 제출상태 sub_status
    @Enumerated(EnumType.STRING)
    private SubStatus subStatus;





}
