package unilearn.unilearn.study.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unilearn.unilearn.study.dto.*;
import unilearn.unilearn.study.entity.Regist;
import unilearn.unilearn.study.exception.UnauthorizedStudyOperationException;
import unilearn.unilearn.study.repository.RegistRepository;
import unilearn.unilearn.study.repository.StdListRepository;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.subject.repository.SubjectRepository;
import unilearn.unilearn.user.dto.MemberDTO;
import unilearn.unilearn.study.entity.StdList;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.exception.StudyNotFoundException;
import unilearn.unilearn.study.repository.StudyRepository;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class StudyService {

    private final UserRepository userRepository;
    @Autowired
    private StudyRepository studyRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    private RegistRepository registRepository;

    private StdListRepository stdListRepository;

    public StudyService(StudyRepository studyRepository, SubjectRepository subjectRepository,UserRepository userRepository,RegistRepository registRepository) {
        this.studyRepository = studyRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.registRepository = registRepository;
    }

    //스터디전체조회(개설과목)
    public List<StudyResponseDTO> getAllStudiesForSubject(Long subjectId) {
        // Subject를 찾아온다.
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isPresent()) {
            // 해당 Subject에 대한 오픈된 스터디 목록을 조회한다.
            List<Study> studies = studyRepository.findBySubjectAndIsOpen(optionalSubject.get(), true);
            // Convert List<Study> to List<StudyResponseDTO>
            return studies.stream()
                    .map(this::mapToStudyResponseDTO)
                    .collect(Collectors.toList());
        } else {
            // 적절한 예외 처리 또는 오류 응답을 구현한다.
            return Collections.emptyList();
        }
    }
    private StudyResponseDTO mapToStudyResponseDTO(Study study) {
        StudyResponseDTO responseDTO = new StudyResponseDTO();
        responseDTO.setStudyId(study.getStudy_id());
        responseDTO.setStudyName(study.getStudy_name());
        responseDTO.setStudyRecruitedNum(study.getStudy_recruited_num());
        responseDTO.setOffline(study.is_offline());
        // Set other properties as needed
        return responseDTO;
    }

    //스터디전체조회(비개설과목)
    public List<NonOpenStudyResponseDTO> getNonOpenStudies() {
        List<Study> nonOpenStudies = studyRepository.findByIsOpenFalseAndStudyStatus("recruiting");

        return nonOpenStudies.stream()
                .map(study -> new NonOpenStudyResponseDTO(
                        study.getStudy_id(),
                        study.getStudy_name(),
                        study.getStudy_recruited_num(),
                        study.is_offline()
                ))
                .collect(Collectors.toList());
    }

    //스터디전체조회(마이페이지 - 참여)
//    public List<MyStudyParticipateDTO> getParticipatedStudies(String userNickname) {
//        // userNickname을 이용하여 user_id를 찾음
//        User user = userRepository.findByNickname(userNickname);
//        log.info("principal user = " + user.toString());
//
//        if (user == null) {
//            // 사용자를 찾을 수 없는 경우 예외 처리 또는 빈 리스트를 반환하는 등의 로직을 추가할 수 있습니다.
//            log.info("user을 찾을 수 없습니다.");
//            return Collections.emptyList();
//        }
//
//        log.info("user.getId() = ",user.getId());
//        log.info("user.getId() = ",user.getLoginId());
//        log.info("user.getId() = ",user.getEmail());
//
//        List<Long> studyIds = stdListRepository.findStudyIdsByUserId(user.getId());
//        if (studyIds == null) {
//            // studyIds가 null인 경우에 대한 예외 처리 또는 빈 리스트를 반환하는 등의 로직을 추가할 수 있습니다.
//            log.info("해당 user_id로는 studyId가 null이다.");
//            return Collections.emptyList();
//        }
//
//        List<Study> participatedStudies = studyRepository.findByStudyIdIn(studyIds);
//
//        return participatedStudies.stream()
//                .map(study -> new MyStudyParticipateDTO(
//                        study.getStudy_id(),
//                        study.getStudy_name(),
//                        study.getStudyLeaderName(),
//                        study.getStudyCurrentNum(),
//                        study.getStudy_recruited_num(),
//                        study.getStudyStatus()
//                ))
//                .collect(Collectors.toList());
//    }
    public List<MyStudyParticipateDTO> getParticipatedStudies(Long userId) {
        // 사용자 정보 조회 (있는 경우)
        User user = userRepository.getUserById(userId);

        // 사용자가 참여한 스터디 목록 조회
        List<Study> participatedStudies = studyRepository.findByUser(user);

        // Study 엔티티를 MyStudyParticipateDTO로 변환
        List<MyStudyParticipateDTO> result = participatedStudies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return result;
    }

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
    public StudyDetailDTO getStudyDetailById(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("Study not found with id: " + studyId));

        return new StudyDetailDTO(study);
    }

    public List<StdList> getStudyMembers(Study study){
//        Study study = studyRepository.findById(studyId)
//                .orElseThrow(()-> new StudyNotFoundException("Study not found with id : "+ studyId));
//
//        List<StdList> stdList = study.getStdList();
//
//        List<MemberDTO> members = new ArrayList<>();
//        for(StdList std : stdList){
//            User user = std.getUser();
//            members.add(new MemberDTO(user.getNickname(), std.getStd_List_id()));
//        }
//
//        return members;
        return study.getStdList();
    }

    public Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("Study not found with id: " + studyId));
    }

    public Subject getSubjectForStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("Study not found with id: " + studyId));

        return study.getSubject();
    }

    public boolean isStudyLeader(Long studyId, String userNickname) {
        // studyId에 해당하는 study 엔티티를 찾음
        Study study = studyRepository.findById(studyId).orElse(null);

        // study가 존재하고, study의 studyLeaderName과 현재 로그인한 유저의 nickname이 일치하는지 확인
        return study != null && study.getStudyLeaderName().equals(userNickname);
    }

    public List<StudyApplicationResponseDTO> getStudyApplications(Long studyId) {
        // studyId에 해당하는  Study 엔터티를 가져옴
        Study study = studyRepository.findById(studyId).orElse(null);

        if (study == null) {
            // 해당 studyId에 대한 스터디가 존재하지 않으면 빈 리스트 반환 또는 예외 처리
            return Collections.emptyList();
        }

        // study에 해당하는 스터디 참여 신청 폼 조회
        List<Regist> studyApplications = registRepository.findByStudy(study);

        // StudyApplicationResponseDTO로 변환
        return studyApplications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Regist를 StudyApplicationResponseDTO로 변환하는 메서드
    private StudyApplicationResponseDTO convertToDTO(Regist regist) {
        StudyApplicationResponseDTO dto = new StudyApplicationResponseDTO();
        dto.setRegistId(regist.getRegist_id());
        dto.setNickname(regist.getUser().getNickname());
        dto.setSchoolName(regist.getUser().getSchool().getSchoolName());
        dto.setMajor(regist.getUser().getMajor());
        return dto;
    }

    //스터디 참여 폼 상세 정보 메서드
    public StudyApplicationDetailDTO getStudyApplicationDetail(Long studyId, Long appliedId) {
        // studyId에 해당하는 스터디 참여 폼 조회
        Optional<Regist> registOptional = registRepository.findById(appliedId);

        if (registOptional.isEmpty() || !registOptional.get().getStudy().getStudy_id().equals(studyId)) {
            // 해당 appliedId에 대한 스터디 참여 폼이 없거나, 스터디와 일치하지 않으면 null 반환
            return null;
        }

        // 스터디 참여 폼 상세 정보를 StudyApplicationDetailDTO로 변환
        return convertToDetailDTO(registOptional.get());
    }

    private StudyApplicationDetailDTO convertToDetailDTO(Regist regist) {
        StudyApplicationDetailDTO dto = new StudyApplicationDetailDTO();
        dto.setRegistDetail(regist.getRegist_detail());
        return dto;
    }

    //스터디 수정
    public boolean updateStudy(Long studyId, StudyUpdateRequestDTO requestDTO) {
        // studyId에 해당하는 스터디 조회
        Optional<Study> studyOptional = studyRepository.findById(studyId);

        if (studyOptional.isEmpty()) {
            // 해당 studyId에 대한 스터디가 없으면 수정 실패
            return false;
        }

        Study study = studyOptional.get();
        // requestDTO를 사용하여 study 엔티티를 업데이트합니다.
        study.update(requestDTO);

        // 업데이트된 스터디를 저장
        studyRepository.save(study);

        return true;
    }

    public void updateStudyStatus(Long studyId, StudyStatusUpdateRequestDTO requestDTO) {
        // 현재 사용자 정보 가져오기
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        // 스터디 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("Study not found with id: " + studyId));

        // 현재 사용자가 스터디장인지 확인
        if (!study.getStudyLeaderName().equals(currentUser)) {
            throw new UnauthorizedStudyOperationException("Only the study leader can update the study status.");
        }

        // 요청된 상태로 업데이트
        study.setStudyStatus(requestDTO.getStudyStatus());

        // 저장
        studyRepository.save(study);
    }
}
