package unilearn.unilearn.study.entity;

import lombok.*;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Transactional
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long study_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String study_name;

    @Column
    private String study_image;

    @Column(nullable = false)
    private String study_status;

    @Column(nullable = false)
    private boolean is_open;

    @Column(nullable = false)
    private String subject_name;

    @Column(nullable = false)
    private String subject_major;

    @Column(nullable = false)
    private String subject_professor;

    @Column(nullable = false)
    private int subject_year;

    @Column(nullable = false)
    private int subject_semester;

    @Column(nullable = false, length = 1000)
    private String study_content;

    @Column(nullable = false)
    private boolean is_offline;

    @Column
    private String offline_location;

    @Column(nullable = false)
    private int study_recruited_num;

    @Column(nullable = false)
    private double study_deposit;

    @Column(nullable = false)
    private LocalDate study_start_day;

    @Column(nullable = false)
    private LocalDate study_deadline;

//    아직 subject 테이블 없어서
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "subject_id", nullable = false)
//    private subject subject;
}
