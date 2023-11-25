package unilearn.unilearn.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StudyUpdateRequestDTO {
    private String studyName;
    private String studyContent;
    private boolean isOffline;
    private String offlineLocation;
    private Integer studyRecruitedNum;
    private Integer studyDeposit;
    private String studyStartDay;
    private String studyDeadline;
    private List<RegularMeetingDTO> regularMeetings;
}



