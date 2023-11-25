package unilearn.unilearn.subject.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import unilearn.unilearn.subject.entity.QuizComment;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class QuizCommentForm {
    private Long id; //댓글 id
    private Long quizId;
    private String comment;
    public static QuizCommentForm createQuizCommentDto(QuizComment quizComment) {
        return new QuizCommentForm(
                quizComment.getId(),
                quizComment.getQuiz().getId(),
                quizComment.getComment()
        );

    }
}
