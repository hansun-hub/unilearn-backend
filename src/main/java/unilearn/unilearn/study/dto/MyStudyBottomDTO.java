package unilearn.unilearn.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import unilearn.unilearn.study.entity.Study;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MyStudyBottomDTO {

    private final Long studyId;
    private final String studyName;
    private final String studyLeaderName;
    private final int studyCurrentNum;
    private final int studyRecruitedNum;
    private final boolean isOffline;

    public static List<MyStudyBottomDTO> fromEntities(List<Study> studies) {
        return studies.stream()
                .map(study -> new MyStudyBottomDTO(
                        study.getStudy_id(),
                        study.getStudy_name(),
                        study.getStudyLeaderName(),
                        study.getStudyCurrentNum(),
                        study.getStudy_recruited_num(),
                        study.is_offline()
                ))
                .collect(Collectors.toList());
    }

}
