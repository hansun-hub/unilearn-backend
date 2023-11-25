package unilearn.unilearn.subject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.subject.Dto.QuizForm;
import unilearn.unilearn.subject.entity.Quiz;
import unilearn.unilearn.subject.repository.QuizRepository;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    // 퀴즈 게시판 모든 게시글 조회
    @GetMapping("/api/quiz")
    public ResponseEntity<List<QuizForm>> getAllQuiz(Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Quiz> quizList = quizRepository.findAll();

        List<QuizForm> returnDtoList = new ArrayList<>();
        for(Quiz quiz : quizList){
            QuizForm returnDto = new QuizForm();
            returnDto.setId(quiz.getId());
            returnDto.setAuthor(quiz.getAuthor().getId());
            returnDto.setContent(quiz.getContent());

            returnDtoList.add(returnDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(returnDtoList);

    }

    // 퀴즈 게시판 특정 게시글 조회
    @GetMapping("/api/quiz/{quiz_id}")
    public ResponseEntity<QuizForm> getQuizById(@PathVariable Long quiz_id, Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Quiz> optionalQuiz = quizRepository.findById(quiz_id);

        if(optionalQuiz.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Quiz quiz = optionalQuiz.get();
        QuizForm returnDto = new QuizForm();
        returnDto.setId(quiz.getId());
        returnDto.setAuthor(quiz.getAuthor().getId());
        returnDto.setContent(quiz.getContent());


        return ResponseEntity.status(HttpStatus.OK).body(returnDto);
    }

    // 퀴즈 게시판 게시글 생성
    @PostMapping("/api/quiz/create")
    public ResponseEntity<QuizForm> create(@Valid @RequestBody QuizForm quizForm, BindingResult bindingResult, Principal principal){
        if (principal.getName() != null){
            System.out.println(principal.getName() + principal);
        }
        else if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());

        Quiz quiz = Quiz.builder()
                .author(user)
                .content(quizForm.getContent())
                .build();
        Quiz saveQuiz = quizRepository.save(quiz);

        QuizForm returnDto = new QuizForm();
        returnDto.setId(saveQuiz.getId());
        returnDto.setAuthor(saveQuiz.getAuthor().getId());
        returnDto.setContent(saveQuiz.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(returnDto);
    }

    // 퀴즈 게시판 게시글 수정
    @PatchMapping("/api/quiz/{quiz_id}")
    public ResponseEntity<QuizForm> updateQuiz(@PathVariable Long quiz_id, @Valid @RequestBody QuizForm quizForm, BindingResult bindingResult, Principal principal){
        if (principal.getName() != null){
            System.out.println(principal.getName() + principal);
        }
        else if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Quiz> optionalQuiz = quizRepository.findById(quiz_id);
        if(optionalQuiz.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User user = userRepository.findByNickname(principal.getName());
        Quiz existingQuiz = optionalQuiz.get();

        if(!existingQuiz.getAuthor().equals(user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existingQuiz.setAuthor(user);
        existingQuiz.setContent(quizForm.getContent());

        Quiz updatedQuiz = quizRepository.save(existingQuiz);

        QuizForm returnDto = new QuizForm();
        returnDto.setId(updatedQuiz.getId());
        returnDto.setAuthor(updatedQuiz.getAuthor().getId());
        returnDto.setContent(updatedQuiz.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(returnDto);
    }

    // 퀴즈 게시판 게시글 삭제
    @DeleteMapping("/api/quiz/{quiz_id}")
    public ResponseEntity<Void> delete(@PathVariable Long quiz_id, Principal principal){
        if (principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Quiz> optionalQuiz = quizRepository.findById(quiz_id);
        if(optionalQuiz.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        quizRepository.deleteById(quiz_id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
