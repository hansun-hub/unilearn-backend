package unilearn.unilearn.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unilearn.unilearn.study.dto.*;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.subject.repository.SubjectRepository;
import unilearn.unilearn.user.dto.MemberDTO;
import unilearn.unilearn.user.entity.RegularMeeting;
import unilearn.unilearn.user.entity.StdList;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.exception.StudyNotFoundException;
import unilearn.unilearn.study.repository.StudyRepository;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudyService {

    @Autowired
    private StudyRepository studyRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    public StudyService(StudyRepository studyRepository, SubjectRepository subjectRepository) {
        this.studyRepository = studyRepository;
        this.subjectRepository = subjectRepository;
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

    //스터디 상세 조회 (스터디 지원할 때 보이는 정보)
    public StudyDetailDTO getStudyDetailById(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("Study not found with id: " + studyId));

        return new StudyDetailDTO(study);
    }

    public List<MemberDTO> getStudyMembers(Long studyId){
        Study study = studyRepository.findById(studyId)
                .orElseThrow(()-> new StudyNotFoundException("Study not found with id : "+ studyId));

        List<StdList> stdList = study.getStdList();

        List<MemberDTO> members = new ArrayList<>();
        for(StdList std : stdList){
            User user = std.getUser();
            members.add(new MemberDTO(user.getNickname(), std.getStd_List_id()));
        }

        return members;
    }

    //스터디생성
    public Long createStudy(StudyCreateRequestDTO requestDTO) {
        Study study = new Study();
        // Set values from requestDTO to study entity
        study.setStudyName(requestDTO.getStudyName());
        study.setIsOpen(requestDTO.isOpen());
        study.setSubjectMajor(requestDTO.getSubjectMajor());
        study.setSubjectName(requestDTO.getSubjectName());
        study.setSubjectProfessor(requestDTO.getSubjectProfessor());
        study.setSubjectYear(requestDTO.getSubjectYear());
        study.setSubjectSemester(requestDTO.getSubjectSemester());
        study.setStudyContent(requestDTO.getStudyContent());
        study.setIsOffline(requestDTO.isOffline());
        study.setOfflineLocation(requestDTO.getOfflineLocation());
        study.setStudyRecruitedNum(requestDTO.getStudyRecruitedNum());
        study.setStudyDeposit(requestDTO.getStudyDeposit());
        study.setStudyStartDay(requestDTO.getStudyStartDay());
        study.setStudyDeadline(requestDTO.getStudyDeadline());
        study.setStudyStatus(requestDTO.getStudyStatus());

        // Set RegularMeetings
        List<RegularMeetingDTO> regularMeetingsDTO = requestDTO.getRegularMeetings();
        if (regularMeetingsDTO != null) {
            Set<RegularMeeting> regularMeetings = regularMeetingsDTO.stream()
                    .map(dto -> RegularMeeting.builder()
                            .day_of_week(dto.getDayOfWeek())
                            .start_time(LocalTime.parse(dto.getStartTime()))
                            .end_time(LocalTime.parse(dto.getEndTime()))
                            .study(study)
                            .build())
                    .collect(Collectors.toSet());

            study.setRegularMeetings(regularMeetings);
        }
        // Save the study entity
        Study savedStudy = studyRepository.save(study);
        return savedStudy.getStudy_id();
    }

}
