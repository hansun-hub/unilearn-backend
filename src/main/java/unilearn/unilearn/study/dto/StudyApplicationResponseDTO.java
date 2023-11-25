package unilearn.unilearn.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyApplicationResponseDTO {
    private Long registId;
    private String nickname;
    private String schoolName;
    private String major;

}
