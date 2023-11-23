package unilearn.unilearn.user.entity;
import lombok.*;
import unilearn.unilearn.study.entity.Study;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Transactional

public class Regist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regist_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

//    subject 테이블 부재
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "subject_id", nullable = false)
//    private Subject subject;

    @Column(nullable = false)
    private String regist_detail;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @Column(nullable = false)
    private String regist_status;
}
