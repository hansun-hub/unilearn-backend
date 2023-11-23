package unilearn.unilearn.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NonOpenStudyResponseDTO {
    private Long studyId;
    private String studyName;
    private int studyRecruitedNum;
    private boolean isOffline;

}
