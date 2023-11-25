package unilearn.unilearn.subject.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectForm {
    private Long id;
    private String department; // 개설학과

    private String className; // 수업명

    private String professor; // 교수님

    private int subjectYear; // 연도

    private int semester; // 학기
}
