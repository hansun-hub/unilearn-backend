package unilearn.unilearn.study.entity;

import lombok.*;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.user.entity.RegularMeeting;
import unilearn.unilearn.user.entity.StdList;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Column(nullable = false,name="study_status")
    private String studyStatus;

    @Column(nullable = false, name="is_open")
    private boolean isOpen;

    @Column(nullable = false)
    private String subject_name;

    @Column(nullable = false)
    private String subject_major;

    @Column(nullable = false)
    private String subject_professor;

    @Column(nullable = false)
    private int subject_year;

    @Column(nullable = false)
    private int subject_semester;

    @Column(nullable = false, length = 1000)
    private String study_content;

    @Column(nullable = false)
    private boolean is_offline;

    @Column
    private String offline_location;

    @Column(nullable = false)
    private int study_recruited_num;

    @Column(nullable = false)
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


}
