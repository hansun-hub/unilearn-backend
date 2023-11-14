package unilearn.unilearn.user.entity;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity @Getter @Builder
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PUBLIC)
@Transactional
public class MySubject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="my_subjects_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    private String sbj_name; // 수강과목
    private String department; // 학과
    private int year; // 개설연도
    private int semester; // 개설학기
    private String professor; // 개설교수
}
