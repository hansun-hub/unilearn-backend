package unilearn.unilearn.study.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.study.dto.*;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.study.repository.StudyRepository;
import unilearn.unilearn.user.dto.MemberDTO;
import unilearn.unilearn.study.service.StudyService;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/studies")
public class StudyController {

    @Autowired
    private StudyService studyService;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    public StudyController(StudyService studyService,UserRepository userRepository, StudyRepository studyRepository) {

        this.studyService = studyService;
        this.userRepository = userRepository;
        this.studyRepository = studyRepository;
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

    @GetMapping("/test")
    public String getPrincipal(Principal principal){
        return principal.getName();
    }
    //스터디 생성
    @PostMapping(path="/new", consumes = "application/json", produces = "application/json")
    public ResponseEntity<StudyCreateRequestDTO> createStudy(@RequestBody StudyCreateRequestDTO requestDTO, BindingResult bindingResult, Principal principal) {
        if (principal.getName() != null){
            System.out.println(principal.getName() + principal);
        }
        else if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //닉네임으로 사용자 조회
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        Study study = Study.builder()
                .user(user)
                .study_name(requestDTO.getStudyName())
                .isOpen(requestDTO.isOpen())
                .subject_major(requestDTO.getSubjectMajor())
                .subject_name(requestDTO.getSubjectName())
                .subject_professor(requestDTO.getSubjectProfessor())
                .subject_year(requestDTO.getSubjectYear())
                .subject_semester(requestDTO.getSubjectSemester())
                .study_content(requestDTO.getStudyContent())
                .is_offline(requestDTO.isOffline())
                .offline_location(requestDTO.getOfflineLocation())
                .study_recruited_num(requestDTO.getStudyRecruitedNum())
                .study_deposit(requestDTO.getStudyDeposit())
                .study_start_day(requestDTO.getStudyStartDay())
                .study_deadline(requestDTO.getStudyDeadline())
                .studyStatus(requestDTO.getStudyStatus())
                .build();
        Study saveStudy = studyRepository.save(study);

        StudyCreateRequestDTO returnDto = new StudyCreateRequestDTO();
        returnDto.setUser(saveStudy.getUser().getId());
        returnDto.setStudyName(saveStudy.getStudy_name());
        returnDto.setOpen(requestDTO.isOpen());
        returnDto.setSubjectMajor(requestDTO.getSubjectMajor());
        returnDto.setSubjectName(requestDTO.getSubjectName());
        returnDto.setSubjectProfessor(requestDTO.getSubjectProfessor());
        returnDto.setSubjectYear(requestDTO.getSubjectYear());
        returnDto.setSubjectSemester(requestDTO.getSubjectSemester());
        returnDto.setStudyContent(requestDTO.getStudyContent());
        returnDto.setOffline(requestDTO.isOffline());
        returnDto.setOfflineLocation(requestDTO.getOfflineLocation());
        returnDto.setStudyRecruitedNum(requestDTO.getStudyRecruitedNum());
        returnDto.setStudyDeposit(requestDTO.getStudyDeposit());
        returnDto.setStudyStartDay(requestDTO.getStudyStartDay());
        returnDto.setStudyDeadline(requestDTO.getStudyDeadline());
        returnDto.setStudyStatus(requestDTO.getStudyStatus());
        returnDto.setRegularMeetings(requestDTO.getRegularMeetings());

        return ResponseEntity.status(HttpStatus.CREATED).body(returnDto);
    }

}
