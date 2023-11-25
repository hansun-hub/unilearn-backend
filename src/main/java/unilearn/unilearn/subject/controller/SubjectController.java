package unilearn.unilearn.subject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unilearn.unilearn.subject.Dto.SubjectForm;
import unilearn.unilearn.subject.Dto.SubjectListResponseDto;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.subject.repository.SubjectRepository;
import unilearn.unilearn.subject.service.SubjectService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    private SubjectRepository subjectRepository;


    // GET 요청을 처리하여 모든 과목 목록 반환
    @GetMapping("/subjects")
    public List<SubjectListResponseDto> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

//    @GetMapping("{id}")
//    public Subject read(@PathVariable Long id) {
//        return subjectService.findById(id);
//    }

    // 개설과목 전체 조회
    @GetMapping("/api/courseOfferings")
    public ResponseEntity<List<SubjectForm>> getAllCourseOfferings() {
        List<Subject> subjectList = subjectRepository.findAll();

        List<SubjectForm> returnDtoList = new ArrayList<>();
        for (Subject subject : subjectList) {
            SubjectForm returnDto = new SubjectForm();
            returnDto.setId(subject.getId());
            returnDto.setDepartment(subject.getDepartment());
            returnDto.setClassName(subject.getClassName());
            returnDto.setProfessor(subject.getProfessor());
            returnDto.setSubjectYear(subject.getYear());
            returnDto.setSemester(subject.getSemester());

            returnDtoList.add(returnDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(returnDtoList);
    }

}
