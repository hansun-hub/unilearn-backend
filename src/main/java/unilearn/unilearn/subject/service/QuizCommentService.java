package unilearn.unilearn.subject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unilearn.unilearn.subject.Dto.QuizCommentForm;
import unilearn.unilearn.subject.entity.Quiz;
import unilearn.unilearn.subject.entity.QuizComment;
import unilearn.unilearn.subject.repository.QuizCommentRepository;
import unilearn.unilearn.subject.repository.QuizRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizCommentService {
    @Autowired
    private QuizCommentRepository quizCommentRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<QuizCommentForm> comments(Long quizId) {
        return quizCommentRepository.findByQuizId(quizId)
                .stream()
                .map(comment -> QuizCommentForm.createQuizCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Transactional
    public QuizCommentForm create(Long quizId, QuizCommentForm quizCommentForm) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패"+" 대상 퀴즈 게시글이 없습니다."));
        QuizComment quizComment = QuizComment.createQuizComment(quizCommentForm, quiz);
        QuizComment created = quizCommentRepository.save(quizComment);
        return QuizCommentForm.createQuizCommentDto(created);
    }


    @Transactional
    public QuizCommentForm delete(Long quizCommentId) {
        QuizComment target = quizCommentRepository.findById(quizCommentId).orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패"+" 대상 댓글 없습니다."));
        quizCommentRepository.delete(target);
        return QuizCommentForm.createQuizCommentDto(target);
    }
}
