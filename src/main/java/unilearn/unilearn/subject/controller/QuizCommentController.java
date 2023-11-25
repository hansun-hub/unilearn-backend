package unilearn.unilearn.subject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.subject.Dto.QuizCommentForm;
import unilearn.unilearn.subject.service.QuizCommentService;
import unilearn.unilearn.user.entity.User;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class QuizCommentController {

    @Autowired
    private QuizCommentService quizCommentService;

    // 댓글 전체 조회
    @GetMapping("/api/quiz/{quizId}/comments")
    public ResponseEntity<List<QuizCommentForm>> comments(@PathVariable Long quizId, Principal principal){
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<QuizCommentForm> dtos= quizCommentService.comments(quizId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 댓글 생성
    @PostMapping("/api/quiz/{quizId}/comments")
    public ResponseEntity<QuizCommentForm> create(@Valid @RequestBody QuizCommentForm quizCommentForm, @PathVariable Long quizId, Principal principal){
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        QuizCommentForm createdQuizCommentForm = quizCommentService.create(quizId, quizCommentForm);
        return ResponseEntity.status(HttpStatus.OK).body(createdQuizCommentForm);
    }

    // 댓글 삭제
    @DeleteMapping("/api/comments/{quizCommentId}")
    public ResponseEntity<QuizCommentForm> delete(@PathVariable Long quizCommentId){
        QuizCommentForm deletedDto = quizCommentService.delete(quizCommentId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
    }
}
