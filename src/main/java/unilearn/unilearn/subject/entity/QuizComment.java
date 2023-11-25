package unilearn.unilearn.subject.entity;

import lombok.*;
import unilearn.unilearn.subject.Dto.QuizCommentForm;

import javax.persistence.*;

// 퀴즈 댓글 엔티티
@Entity
@Table(name="quizcomment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QuizComment {

    @Id @Column(name="quizcomment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz; // 퀴즈(Quiz랑 연관관계 설정 퀴즈 하나당 여러개의 퀴즈 댓글)

    private String comment; // 댓글 내용


    public static QuizComment createQuizComment(QuizCommentForm quizCommentForm, Quiz quiz) {
        // 예외 발생
        if (quizCommentForm.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패; 댓글의 id가 없어야 합니다.");
        if (quizCommentForm.getQuizId() != quiz.getId())
            throw new IllegalArgumentException("댓글 생성 실패;게시글의 id가 잘못됐습니다.");

        // 엔티티 생성 및 반환
        return new QuizComment(
                quizCommentForm.getId(),
                quiz,
                quizCommentForm.getComment()
        );
    }
}
