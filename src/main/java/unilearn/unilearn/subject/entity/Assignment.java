package unilearn.unilearn.subject.entity;

import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import java.util.Date;

// 과제 엔티티
@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; // 개설과목

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author; // 작성자

    private String content; // 내용

    private String photo; // 사진

    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline; // *마감일
}