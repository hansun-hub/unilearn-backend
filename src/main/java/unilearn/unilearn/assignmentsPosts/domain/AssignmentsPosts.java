package unilearn.unilearn.assignmentsPosts.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import unilearn.unilearn.assignmentsSubmitPosts.domain.AssignmentsSubmitPosts;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// 스터디 - 과제 게시글
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
    private Study study;*/

    @OneToMany(mappedBy = "assignmentsPosts", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssignmentsSubmitPosts> assignmentSubmitPosts;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    private String status;
    private String title;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deadline;
    private long submit_num = 0;
    private String submit_name = "";

    //생성 시간을 자동으로 채우는 메서드
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        updateStatus();
    }

    //수정 시간을 자동으로 채우는 메서드
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        updateStatus();
    }
    // status 자동 변환 메서드

    public void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(deadline)) {
            this.status = "after";
        } else {
            this.status = "before";
        }
    }




}
