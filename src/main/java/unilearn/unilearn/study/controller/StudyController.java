package unilearn.unilearn.study.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.study.Enum.StudyStatus;
import unilearn.unilearn.study.dto.*;
import unilearn.unilearn.study.entity.Regist;
import unilearn.unilearn.study.entity.StdList;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.study.exception.ResourceNotFoundException;
import unilearn.unilearn.study.exception.UnauthorizedStudyOperationException;
import unilearn.unilearn.study.repository.RegistRepository;
import unilearn.unilearn.study.repository.StdListRepository;
import unilearn.unilearn.study.repository.StudyRepository;
import unilearn.unilearn.study.service.RegistService;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.user.dto.MemberDTO;
import unilearn.unilearn.study.service.StudyService;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.exception.StudyNotFoundException;
import unilearn.unilearn.user.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/studies")
public class StudyController {

    @Autowired
    private StudyService studyService;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final RegistService registService;

    private final RegistRepository registRepository;

    private final StdListRepository stdListRepository;

    public StudyController(StudyService studyService,UserRepository userRepository, StudyRepository studyRepository,RegistService registService,RegistRepository registRepository,StdListRepository stdListRepository) {

        this.studyService = studyService;
        this.userRepository = userRepository;
        this.studyRepository = studyRepository;
        this.registService = registService;
        this.registRepository = registRepository;
        this.stdListRepository = stdListRepository;
    }

//    @GetMapping("/{study_id}/members")
//    public ResponseEntity<List<MemberDTO>> getStudyMembers(
//            @PathVariable("study_id") Long studyId){
//        List<MemberDTO> members = studyService.getStudyMembers(studyId);
//        return new ResponseEntity<>(members, HttpStatus.OK);
//    }

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

    //스터디전체조회(하단바)
    @GetMapping("/my/all")
    public ResponseEntity<List<MyStudyBottomDTO>> getMyStudies(Principal principal) {
        String nickname = principal.getName();
        User user = userRepository.findByNickname(nickname);

        List<Study> myStudies = studyRepository.findByUser(user);
        List<MyStudyBottomDTO> myStudyBottomDTOs = MyStudyBottomDTO.fromEntities(myStudies);

        return ResponseEntity.status(HttpStatus.OK).body(myStudyBottomDTOs);

    }

    //스터디 전체 조회(마이페이지 - 참여)
//    @GetMapping("/my/participated")
//    public ResponseEntity<List<MyStudyParticipateDTO>> getParticipatedStudies(Principal principal) {
//
//        String userNickname = principal.getName();
//        User user = userRepository.findByNickname(userNickname);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        List<Study> participatedStudies = studyService.getParticipatedStudies(userNickname);
//        List<MyStudyParticipateDTO> resultDTOs = participatedStudies.stream()
//                .filter(study -> !userNickname.equals(study.getStudyLeaderName()))
//                .map(study -> new MyStudyParticipateDTO(
//                        study.getStudy_id(),
//                        study.getStudy_name(),
//                        study.getStudyLeaderName(),
//                        study.getStudyCurrentNum(),
//                        study.getStudy_recruited_num(),
//                        study.getStudyStatus().toString()
//                ))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(resultDTOs);
//    }
//    @GetMapping("/my/participated")
//    public ResponseEntity<List<MyStudyParticipateDTO>> getParticipatedStudies(Principal principal) {
//        String userNickname = principal.getName();
//        List<MyStudyParticipateDTO> resultDTOs = studyService.getParticipatedStudies(userNickname);
//        return ResponseEntity.ok(resultDTOs);
//    }
    @GetMapping("/my/participated")
    public ResponseEntity<List<MyStudyParticipateDTO>> getParticipatedStudies(Principal principal) {
        try {
            // 현재 사용자 정보 가져오기
            String nickname = principal.getName();
            User user = userRepository.findByNickname(nickname);

            // stdList 테이블에서 현재 사용자가 참여한 study_id 목록 가져오기
            List<Long> participatedStudyIds = stdListRepository.findStudyIdsByUserId(user.getId());

            // 참여한 스터디 목록 조회
            List<Study> participatedStudies = studyRepository.findByStudyIdIn(participatedStudyIds);

            // MyStudyParticipateDTO로 변환
            List<MyStudyParticipateDTO> myStudyParticipateDTOs = participatedStudies.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(myStudyParticipateDTOs);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // StudyController에 추가할 private 메서드
    private MyStudyParticipateDTO convertToDTO(Study study) {
        return MyStudyParticipateDTO.builder()
                .studyId(study.getStudy_id())
                .studyName(study.getStudy_name())
                .studyLeaderName(study.getStudyLeaderName())
                .studyCurrentNum(study.getStudyCurrentNum())
                .studyRecruitedNum(study.getStudy_recruited_num())
                .studyStatus(study.getStudyStatus())
                .build();
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
    public ResponseEntity<StudyCreateRequestDTO> createStudy(@RequestBody @Valid StudyCreateRequestDTO requestDTO, BindingResult bindingResult, Principal principal) {
        if (principal.getName() != null){
            System.out.println(principal.getName() + principal);
        }
        else if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String studyLeaderName = principal.getName();
        int studyCurrentNum = 1;
        String studyStatus = "RECRUITING";

        //닉네임으로 사용자 조회
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        Study study = Study.builder()
                .user(user)
                .studyLeaderName(studyLeaderName)
                .studyCurrentNum(studyCurrentNum)
                .studyStatus(studyStatus)
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
        returnDto.setRegularMeetings(requestDTO.getRegularMeetings());

        return ResponseEntity.status(HttpStatus.CREATED).body(returnDto);
    }

    //스터디 내용 수정
    @PutMapping("/{study_id}")
    public ResponseEntity<Void> updateStudy(
            @PathVariable Long study_id,
            @RequestBody StudyUpdateRequestDTO requestDTO,
            Principal principal) {

        // principal을 이용하여 현재 유저의 이름을 가져옵니다.
        String currentUserName = principal.getName();

        // 스터디장 여부 확인
        boolean isStudyLeader = studyService.isStudyLeader(study_id, currentUserName);
        if (!isStudyLeader) {
            // 스터디장이 아니면 권한이 없음
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 스터디 내용 수정 로직을 작성하여 study 엔티티를 업데이트합니다.
        boolean isUpdated = studyService.updateStudy(study_id, requestDTO);

        if (isUpdated) {
            // 수정 성공
            return ResponseEntity.ok().build();
        } else {
            // 수정 실패 (예를 들어, 스터디가 존재하지 않는 경우)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //스터디 참여폼 작성
    @PostMapping("/{studyId}/apply")
    public ResponseEntity<?> applyForStudy(@PathVariable Long studyId, @RequestBody RegistDto registDto,Principal principal) {

        // Principal을 이용해 현재 사용자 정보를 가져옴
        User user = userRepository.findByNickname(principal.getName());

        // Study와 Subject 정보는 service에서 가져오도록 함
        Study study = studyService.getStudyById(studyId);
        Subject subject = studyService.getSubjectForStudy(studyId);

        // RegistDto를 Regist 엔티티로 변환
        Regist regist = Regist.builder()
                .user(user)
                .study(study)
                .subject(subject)
                .regist_detail(registDto.getRegistDetail())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .regist_status("PENDING")  // 예시로 PENDING 상태로 설정
                .build();

        // 서비스에 참여 신청 로직 위임
        registService.applyForStudy(regist);

        // 성공 응답 반환
        return ResponseEntity.ok("스터디 신청이 완료되었습니다.");
    }

    //스터디 참여 폼 제출 취소
    @GetMapping("/{study_id}/apply/{regist_id}/cancel")
    public ResponseEntity<String> cancelStudyForm(
            @PathVariable Long study_id,
            @PathVariable Long regist_id
    ) {
        // 여기에 인증 처리 로직을 추가할 수 있습니다.

        // regist_id에 해당하는 Regist 엔티티를 찾아서 상태를 "cancel"로 업데이트합니다.
        Regist regist = findRegistById(regist_id);
        if (regist != null) {
            regist.setRegist_status("cancel");
            // 상태 업데이트 후 저장
            registRepository.save(regist);

            return ResponseEntity.ok("스터디 참여 폼 제출이 취소되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // regist_id에 해당하는 Regist 엔티티를 찾는 메서드
    private Regist findRegistById(Long regist_id) {
        return registRepository.findById(regist_id).orElse(null);
    }


    //스터디 참여 폼 승인 / 거절
    @PatchMapping("/{study_id}/apply/{regist_id}")
    public ResponseEntity<String> handleStudyFormApprovalOrRejection(
            @PathVariable Long study_id,
            @PathVariable Long regist_id,
            @RequestBody ApprovalOrRejectionRequestDTO approvalOrRejectionRequestDto,
            Principal principal) {

        // 스터디장 여부 확인
        boolean isStudyLeader = studyService.isStudyLeader(study_id, principal.getName());
        if (!isStudyLeader) {
            // 스터디장이 아니면 권한이 없음
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permission denied");
        }

        // regist_id에 해당하는 regist 찾기
        Regist regist = registRepository.findById(regist_id)
                .orElseThrow(() -> new ResourceNotFoundException("Regist not found with id: " + regist_id));

        // ApprovalOrRejectionRequestDTO에서 regist_status 가져오기
        String registStatus = approvalOrRejectionRequestDto.getRegistStatus();

        // regist_status가 null이 아닌 경우에만 설정
        if (registStatus != null) {
            regist.setRegist_status(registStatus);
        }else{
            log.info("registStatus가 null이야");
        }
        // 상태 업데이트 후 저장
        registRepository.save(regist);

        // regist_status가 "accepted"인 경우에만 처리
        if ("accepted".equals(registStatus)) {
            // StdList 엔티티 생성 및 값 설정
            StdList stdList = StdList.builder()
                    .user(regist.getUser())         // 유저 정보 설정
                    .study(regist.getStudy())       // 스터디 정보 설정
                    .subject(regist.getSubject())   // 과목 정보 설정
                    .build();

            // StdList 저장
            stdListRepository.save(stdList);

            // study 엔티티 가져오기
            Optional<Study> optionalStudy = studyRepository.findById(study_id);
            if (optionalStudy.isPresent()) {
                Study study = optionalStudy.get();

                // studyCurrentNum 증가
                int currentNum = study.getStudyCurrentNum();
                study.setStudyCurrentNum(currentNum + 1);

                // study 업데이트
                studyRepository.save(study);
            } else {
                // study가 존재하지 않으면 예외 처리 또는 적절한 조치 수행
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Study not found with id: " + study_id);
            }
        }

        return ResponseEntity.ok("Regist status updated successfully");
    }

    //스터디 참여 폼 조회
    @GetMapping("/{study_id}/apply")
    public ResponseEntity<List<StudyApplicationResponseDTO>> getStudyApplications(
            @PathVariable Long study_id,
            Principal principal) {

        // 스터디장 여부 확인
        boolean isStudyLeader = studyService.isStudyLeader(study_id, principal.getName());
        if (!isStudyLeader) {
            // 스터디장이 아니면 권한이 없음
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }

        // 스터디 참여 폼 조회 로직을 작성하여 studyApplications 리스트를 얻어옵니다.
        List<StudyApplicationResponseDTO> studyApplications = studyService.getStudyApplications(study_id);

        return ResponseEntity.ok(studyApplications);
    }

    //스터디 참여 폼 신청자 정보 상세조회
    @GetMapping("/{study_id}/apply/{applied_id}")
    public ResponseEntity<StudyApplicationDetailDTO> getStudyApplicationDetail(
            @PathVariable Long study_id,
            @PathVariable Long applied_id,
            Principal principal) {

        // 스터디장 여부 확인
        boolean isStudyLeader = studyService.isStudyLeader(study_id, principal.getName());
        if (!isStudyLeader) {
            // 스터디장이 아니면 권한이 없음
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // 스터디 참여 폼 상세 정보 조회 로직을 작성하여 studyApplicationDetail을 얻어옵니다.
        StudyApplicationDetailDTO studyApplicationDetail = studyService.getStudyApplicationDetail(study_id, applied_id);

        if (studyApplicationDetail == null) {
            // 해당 applied_id에 대한 스터디 참여 폼이 없으면 404 에러 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(studyApplicationDetail);
    }

    //스터디 상태 수정 (운영중 / 운영종료)
    @PatchMapping("/{studyId}/status")
    public ResponseEntity<String> updateStudyStatus(
            @PathVariable Long studyId,
            @RequestBody StudyStatusUpdateRequestDTO requestDTO
    ) {
        try {
            studyService.updateStudyStatus(studyId, requestDTO);
            return new ResponseEntity<>("Study status updated successfully", HttpStatus.OK);
        } catch (StudyNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedStudyOperationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    //스터디원 조회
    @GetMapping("/{studyId}/members")
    public ResponseEntity<List<StdListDTO>> getStudyMembers(
            @PathVariable Long studyId,
            Principal principal
    ) {
        try {
            // Principal을 이용해 현재 사용자 정보를 가져옴
            User user = userRepository.findByNickname(principal.getName());

            // Study 정보 가져오기
            Study study = studyService.getStudyById(studyId);

            // 해당 스터디의 스터디원 정보 가져오기
            List<StdList> stdList = studyService.getStudyMembers(study);

            // 스터디원 정보를 DTO로 변환
            List<StdListDTO> stdListDTOs = stdList.stream()
                    .map(std -> {
                        StdListDTO stdListDTO = new StdListDTO();
                        stdListDTO.setStdListId(std.getStd_List_id());
                        stdListDTO.setNickname(std.getUser().getNickname());
                        stdListDTO.setIntroduction(std.getUser().getIntroduction());
                        return stdListDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(stdListDTOs);
        } catch (Exception e) {
            // 예외 발생 시 에러 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
