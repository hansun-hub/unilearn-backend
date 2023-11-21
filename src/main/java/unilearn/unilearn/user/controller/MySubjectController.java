package unilearn.unilearn.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.user.dto.MySubjectRequestDto;
import unilearn.unilearn.user.dto.MySubjectResponseDto;
import unilearn.unilearn.user.repository.MySubjectRepository;
import unilearn.unilearn.user.service.MySubjectService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MySubjectController {
    private final MySubjectRepository mySubjectRepository;
    private final MySubjectService mySubjectService;

    @PostMapping("/course-registration")
    public ResponseEntity<MySubjectResponseDto.MySubjectResponseForm> createMySubject(@RequestBody @Valid MySubjectRequestDto.MySubjectRequestForm form, Principal principal, BindingResult bindingResult) {
        if (principal.getName() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MySubjectResponseDto.MySubjectResponseForm responseForm = mySubjectService.createMySubject(form, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseForm);
    }

    @DeleteMapping("/course/{my_sbj_id}")
    public ResponseEntity<MySubjectResponseDto.MySubjectResponseForm> deleteMySubject(@PathVariable("my_sbj_id") Long mySbjId, Principal principal){
        if (principal.getName() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MySubjectResponseDto.MySubjectResponseForm responseForm = mySubjectService.deleteMySubject(mySbjId, principal);
        if (responseForm == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseForm);
    }

    @GetMapping("/user_courses")
    public ResponseEntity<List<MySubjectResponseDto.MySubjectResponseListForm>> list(Principal principal) {
        if (principal.getName() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<MySubjectResponseDto.MySubjectResponseListForm> list = mySubjectService.list(principal);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
