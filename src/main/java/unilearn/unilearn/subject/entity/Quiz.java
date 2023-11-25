package unilearn.unilearn.subject.entity;

import lombok.*;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import java.util.Date;

// 퀴즈 게시글 엔티티
@Entity
@Table(name="quiz")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Quiz {

    @Id @Column(name="quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "subject_id")
//    private Subject subject; // 개설과목(Subject랑 연관관계 설정 개설과목 1개당 여러개의 퀴즈)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author; // 작성자

    private String content; // 내용


}