package unilearn.unilearn.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyStudyParticipateDTO {
    private Long studyId;
    private String studyName;
    private String studyLeaderName;
    private int studyCurrentNum;
    private int studyRecruitedNum;
    private String studyStatus;

}
