package unilearn.unilearn.subject.entity;

import javax.persistence.*;

// 퀴즈 댓글 엔티티
@Entity
public class QuizComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz; // 퀴즈(Quiz랑 연관관계 설정 퀴즈 하나당 여러개의 퀴즈 댓글)

    private String comment; // 댓글 내용
}
