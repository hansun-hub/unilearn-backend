package unilearn.unilearn.study.entity;

import lombok.*;


import unilearn.unilearn.study.Enum.StudyStatus;
import unilearn.unilearn.study.dto.RegularMeetingDTO;
import unilearn.unilearn.study.dto.StudyUpdateRequestDTO;
import unilearn.unilearn.subject.entity.Subject;

import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Transactional
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long study_id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String study_name;

    @Column
    private String study_image;

    @Builder.Default
    @Column(name = "studyLeaderName")
    private String studyLeaderName= "defaultStudyLeaderName";

    @Builder.Default
    @Column(name = "studyCurrentNum")
    private int studyCurrentNum=1;

    @Builder.Default
    @Column(name="study_status")
    private String studyStatus = "RECRUITING";

    @Column(nullable = false, name="is_open")
    private boolean isOpen;

    @Column(nullable = true)
    private String subject_name;

    @Column(nullable = true)
    private String subject_major;

    @Column(nullable = true)
    private String subject_professor;

    @Column(nullable = true)
    private int subject_year;

    @Column(nullable = true)
    private int subject_semester;

    @Column(nullable = false, length = 1000)
    private String study_content;

    @Column(nullable = false)
    private boolean is_offline;

    @Column(nullable = true)
    private String offline_location;

    @Column(nullable = false)
    private int study_recruited_num;

    @Column(nullable = true)
    private double study_deposit;

    @Column(nullable = false)
    private LocalDate study_start_day;

    @Column(nullable = false)
    private LocalDate study_deadline;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private Set<StdList> stdList;

    public List<StdList> getStdList() {

        return new ArrayList<>(stdList);
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;

    @Column(name = "subject_id", insertable = false, updatable = false)
    private Long subject_id;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private Set<RegularMeeting> regularMeetings;

    @OneToMany(mappedBy = "study")
    private List<Regist> studyRegists;

    public void setSubject(Subject subject) {
        if (subject != null) {
            this.subject_id = subject.getId();
        } else {
            this.subject_id = null;
        }
    }

    public void setStudyName(String studyName) {
        this.study_name = studyName;
    }
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setSubjectMajor(String subjectMajor) {
        this.subject_major = subjectMajor;
    }

    public void setSubjectName(String subjectName) {
        this.subject_name = subjectName;
    }

    public void setSubjectProfessor(String subjectProfessor) {
        this.subject_professor = subjectProfessor;
    }

    public void setSubjectYear(int subjectYear) {
        this.subject_year = subjectYear;
    }

    public void setSubjectSemester(int subjectSemester) {
        this.subject_semester = subjectSemester;
    }

    public void setStudyContent(String studyContent) {
        this.study_content = studyContent;
    }

    public void setIsOffline(boolean isOffline) {
        this.is_offline = isOffline;
    }

    public void setOfflineLocation(String offlineLocation) {
        this.offline_location = offlineLocation;
    }

    public void setStudyRecruitedNum(int studyRecruitedNum) {
        this.study_recruited_num = studyRecruitedNum;
    }

    public void setStudyDeposit(double studyDeposit) {
        this.study_deposit = studyDeposit;
    }

    public void setStudyStartDay(LocalDate studyStartDay) {
        this.study_start_day = studyStartDay;
    }

    public void setStudyDeadline(LocalDate studyDeadline) {
        this.study_deadline = studyDeadline;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(User user) {
        this.user = user;
    }

    public void update(StudyUpdateRequestDTO requestDTO) {
        // requestDTO에 따라 필요한 필드를 업데이트합니다.
        if (requestDTO.getStudyName() != null) {
            this.study_name = requestDTO.getStudyName();
        }

        if (requestDTO.getStudyContent() != null) {
            this.study_content = requestDTO.getStudyContent();
        }

        if (requestDTO.isOffline()) {
            this.is_offline = requestDTO.isOffline();
        }

        if (requestDTO.getOfflineLocation() != null) {
            this.offline_location = requestDTO.getOfflineLocation();
        }

        if (requestDTO.getStudyRecruitedNum() != null) {
            this.study_recruited_num = requestDTO.getStudyRecruitedNum();
        }

        if (requestDTO.getStudyDeposit() != null) {
            this.study_deposit = requestDTO.getStudyDeposit();
        }

        if (requestDTO.getStudyStartDay() != null) {
            this.study_start_day = LocalDate.parse(requestDTO.getStudyStartDay());
        }

        if (requestDTO.getStudyDeadline() != null) {
            this.study_deadline = LocalDate.parse(requestDTO.getStudyDeadline());
        }
    }

    private void updateRegularMeetings(List<RegularMeetingDTO> regularMeetings) {
        // 정기모임 날짜 배열을 업데이트하는 로직을 작성
        // 예시: this.regularMeetings = regularMeetings;
        if (regularMeetings != null) {
            // Create a new set to store the updated RegularMeeting entities
            Set<RegularMeeting> updatedRegularMeetings = new HashSet<>();

            // Convert RegularMeetingDTOs to RegularMeetings and add them to the set
            for (RegularMeetingDTO regularMeetingDTO : regularMeetings) {
                RegularMeeting regularMeeting = new RegularMeeting();
                regularMeeting.setDay_of_week(regularMeetingDTO.getDayOfWeek());
                regularMeeting.setStart_time(LocalTime.parse(regularMeetingDTO.getStartTime()));
                regularMeeting.setEnd_time(LocalTime.parse(regularMeetingDTO.getEndTime()));
                regularMeeting.setStudy(this); // Set the reference to the owning study
                updatedRegularMeetings.add(regularMeeting);
            }

            // Update the regularMeetings field with the new set
            this.regularMeetings = updatedRegularMeetings;
        }
    }
    public static class StudyBuilder {
//        private String studyLeaderName = "defaultStudyLeaderName";
        private int studyCurrentNum = 1;
        private String studyStatus = "RECRUITING";

//        public StudyBuilder studyStatus(StudyStatus studyStatus) {
//            this.studyStatus = studyStatus;
//            return this;
//        }


    }

}
