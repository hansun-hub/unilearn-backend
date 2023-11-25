package unilearn.unilearn.assignmentsSubmitPosts.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentsPosts;
import unilearn.unilearn.assignmentsSubmitPosts.constant.SubStatus;
import unilearn.unilearn.evaluation.entity.Evaluation;

import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.List;

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

    //private String img;
    //과제랑 연결해야해서 추가했습니다 - 서윤
    @ManyToOne
    @JoinColumn(name = "assignments_posts_id")
    @JsonIgnore
    private AssignmentsPosts assignmentsPosts;
    /*@ManyToOne
    @JoinColumn(name="study_id")
    private Study study;*/



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    /* 이건 assignmentsPosts에서 가져오겠습니다.
    private String status; */
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    /*private Long evaluation; 평가 부분 엔티티로 대체 가능하기 때문에 빼겠습니다 - 서윤*/
    @OneToMany(mappedBy = "assignmentsSubmitPosts", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Evaluation> evaluations;

    private  int total_score;
    private  String feedback="";
    // enum 제출상태 sub_status - 지각인지 정상인지 구분
    @Enumerated(EnumType.STRING)
    private SubStatus subStatus;

    // 생성 시간을 자동으로 채우는 메서드
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

    }

    // 수정 시간을 자동으로 채우는 메서드
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();

    }


   





}
