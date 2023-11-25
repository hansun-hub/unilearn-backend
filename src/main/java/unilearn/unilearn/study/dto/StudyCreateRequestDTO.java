package unilearn.unilearn.study.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter @Setter
public class StudyCreateRequestDTO {

    private Long user;
    private String studyName;
    private boolean isOpen;
    private String subjectMajor;
    private String subjectName;
    private String subjectProfessor;
    private int subjectYear;
    private int subjectSemester;
    private String studyContent;
    private boolean isOffline;
    private String offlineLocation;
    private int studyRecruitedNum;
    private int studyDeposit;
    private LocalDate studyStartDay;
    private LocalDate studyDeadline;
    private List<RegularMeetingDTO> regularMeetings;
}

