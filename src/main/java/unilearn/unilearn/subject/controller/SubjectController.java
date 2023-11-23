package unilearn.unilearn.subject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unilearn.unilearn.subject.Dto.SubjectListResponseDto;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.subject.service.SubjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    // GET 요청을 처리하여 모든 과목 목록 반환
    @GetMapping("/subjects")
    public List<SubjectListResponseDto> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

//    @GetMapping("{id}")
//    public Subject read(@PathVariable Long id) {
//        return subjectService.findById(id);
//    }
}
