package unilearn.unilearn.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import unilearn.unilearn.study.entity.Study;

@Getter
@Setter
@AllArgsConstructor
public class StudyDetailDTO {
    private Long studyId;
    private String studyName;
    private String studyDetail;
    private int studyCurrentNum;
    private int studyRecruitedNum;
    private double studyDeposit;
    private String studyStartDay;
    private String studyDeadline;
    private String studyLocation;
    private String studyStatus;

    public StudyDetailDTO(Study study) {
        this.studyId = study.getStudy_id();
        this.studyName = study.getStudy_name();
        this.studyDetail = study.getStudy_content();
        this.studyCurrentNum = study.getStdList().size();
        this.studyRecruitedNum = study.getStudy_recruited_num();
        this.studyDeposit = study.getStudy_deposit();
        this.studyStartDay = study.getStudy_start_day().toString();
        this.studyDeadline = study.getStudy_deadline().toString();
        this.studyLocation = study.is_offline() ? study.getOffline_location() : "비대면";
        this.studyStatus = study.getStudyStatus();
    }
}
