package unilearn.unilearn.subject.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.subject.Dto.AssignmentForm;
import unilearn.unilearn.subject.entity.Assignment;
import unilearn.unilearn.subject.repository.AssignmentRepository;
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
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    // 과제 게시판 모든 게시글 조회
    @GetMapping("/api/assignments")
    public ResponseEntity<List<AssignmentForm>> getAllAssignments() {
        List<Assignment> assignmentsList = assignmentRepository.findAll();

        List<AssignmentForm> returnDtoList = new ArrayList<>();
        for (Assignment assignment : assignmentsList) {
            AssignmentForm returnDto = new AssignmentForm();
            returnDto.setId(assignment.getId());
            returnDto.setAuthor(assignment.getAuthor().getId());
            returnDto.setContent(assignment.getContent());

            returnDtoList.add(returnDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(returnDtoList);
    }

    // 과제 게시판 특정 게시글 조회
    @GetMapping("/api/assignments/{assignment_id}")
    public ResponseEntity<AssignmentForm> getAssignmentById(@PathVariable Long assignment_id) {
        Optional<Assignment> optionalAssignments = assignmentRepository.findById(assignment_id);

        if (optionalAssignments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Assignment assignments = optionalAssignments.get();
        AssignmentForm returnDto = new AssignmentForm();
        returnDto.setId(assignments.getId());
        returnDto.setAuthor(assignments.getAuthor().getId());
        returnDto.setContent(assignments.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(returnDto);
    }

    // 과제 게시판 게시글 생성
    @PostMapping("/api/assignments/create")
    public ResponseEntity<AssignmentForm> create(@Valid @RequestBody AssignmentForm assignmentForm, BindingResult bindingResult, Principal principal) {
        if (principal == null || bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());

        Assignment assignments = Assignment.builder()
                .author(user)
                .content(assignmentForm.getContent())
                .build();

        Assignment saveAssignments = assignmentRepository.save(assignments);

        AssignmentForm returnDto = new AssignmentForm();
        returnDto.setId(saveAssignments.getId());
        returnDto.setAuthor(saveAssignments.getAuthor().getId());
        returnDto.setContent(saveAssignments.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(returnDto);
    }

    // 과제 게시판 게시글 수정
    @PatchMapping("/api/assignments/{assignment_id}")
    public ResponseEntity<AssignmentForm> updateAssignment(@PathVariable Long assignment_id, @Valid @RequestBody AssignmentForm assignmentsForm, BindingResult bindingResult, Principal principal) {
        if (principal == null || bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Assignment> optionalAssignmentsPosts = assignmentRepository.findById(assignment_id);
        if (optionalAssignmentsPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userRepository.findByNickname(principal.getName());
        Assignment existingAssignmentsPosts = optionalAssignmentsPosts.get();

        if (!existingAssignmentsPosts.getAuthor().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existingAssignmentsPosts.setContent(assignmentsForm.getContent());

        Assignment updatedAssignmentsPosts = assignmentRepository.save(existingAssignmentsPosts);

        AssignmentForm returnDto = new AssignmentForm();
        returnDto.setId(updatedAssignmentsPosts.getId());
        returnDto.setAuthor(updatedAssignmentsPosts.getAuthor().getId());
        returnDto.setContent(updatedAssignmentsPosts.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(returnDto);
    }

    // 과제 게시판 게시글 삭제
    @DeleteMapping("/api/assignments/{assignment_id}")
    public ResponseEntity<Void> delete(@PathVariable Long assignment_id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Assignment> optionalAssignmentsPosts = assignmentRepository.findById(assignment_id);
        if (optionalAssignmentsPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userRepository.findByNickname(principal.getName());
        Assignment existingAssignmentsPosts = optionalAssignmentsPosts.get();

        if (!existingAssignmentsPosts.getAuthor().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        assignmentRepository.deleteById(assignment_id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
