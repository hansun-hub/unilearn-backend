package unilearn.unilearn.subject.entity;

import lombok.*;
import unilearn.unilearn.global.entity.BaseTimeEntity;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import java.util.Date;

// 과제 엔티티
@Entity
@Table(name="assignment")
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
@Builder
public class Assignment extends BaseTimeEntity {

    @Id    @Column(name="assignment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; // 개설과목

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author; // 작성자

    private String content; // 내용



}