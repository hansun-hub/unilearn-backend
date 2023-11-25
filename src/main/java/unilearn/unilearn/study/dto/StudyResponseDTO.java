package unilearn.unilearn.study.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unilearn.unilearn.study.entity.Study;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyResponseDTO {
    private Long studyId;
    private String studyName;
    private int studyRecruitedNum;
    private boolean isOffline;

    // 기본 생성자
//    public StudyResponseDTO() {
//    }

}
