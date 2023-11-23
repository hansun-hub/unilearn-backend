package unilearn.unilearn.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.study.dto.*;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.user.dto.MemberDTO;
import unilearn.unilearn.study.service.StudyService;
import unilearn.unilearn.user.entity.User;

import java.util.List;

@RestController
@RequestMapping("/studies")
public class StudyController {

    @Autowired
    private StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @GetMapping("/{study_id}/members")
    public ResponseEntity<List<MemberDTO>> getStudyMembers(
            @PathVariable("study_id") Long studyId){
        List<MemberDTO> members = studyService.getStudyMembers(studyId);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    //스터디전체조회(개설과목)
    @GetMapping("/{subjectId}/all")
    public ResponseEntity<List<StudyResponseDTO>> getAllStudiesForSubject(@PathVariable Long subjectId) {
        List<StudyResponseDTO> studies = studyService.getAllStudiesForSubject(subjectId);
        return new ResponseEntity<>(studies, HttpStatus.OK);
    }


    //스터디전체조회(비개설과목)
    @GetMapping("/freedom/all")
    public ResponseEntity<List<NonOpenStudyResponseDTO>> getAllNonOpenStudies() {
        List<NonOpenStudyResponseDTO> nonOpenStudies = studyService.getNonOpenStudies();
        return ResponseEntity.ok(nonOpenStudies);
    }

    //스터디 상세 조회 (스터디 지원할 때 보이는 정보)
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDetailDTO> getStudyDetail(@PathVariable Long studyId) {
        StudyDetailDTO studyDetailDTO = studyService.getStudyDetailById(studyId);
        return ResponseEntity.ok(studyDetailDTO);
    }

    //스터디 생성
    @PostMapping(path="/new", consumes = "application/json")
    public ResponseEntity<StudyCreateResponseDTO> createStudy(@RequestBody StudyCreateRequestDTO requestDTO) {

        // Study 생성
        Long studyId = studyService.createStudy(requestDTO);

        // 응답 DTO 생성 및 반환
        StudyCreateResponseDTO responseDTO = new StudyCreateResponseDTO(studyId);
        return ResponseEntity.ok(responseDTO);
    }

}
