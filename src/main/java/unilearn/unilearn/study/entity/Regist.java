package unilearn.unilearn.study.entity;
import lombok.*;

import unilearn.unilearn.study.entity.Study;

import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Entity
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(nullable = false)
    private String regist_detail;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @Column(nullable = false)
    private String regist_status;
}
