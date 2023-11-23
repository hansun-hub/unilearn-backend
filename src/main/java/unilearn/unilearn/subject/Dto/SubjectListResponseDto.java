package unilearn.unilearn.subject.Dto;

import lombok.Getter;
import unilearn.unilearn.subject.entity.Subject;

@Getter
public class SubjectListResponseDto {
    private Long id;
    private String department; // 개설학과
    private String className; // 수업명


    public SubjectListResponseDto(Subject entity){
        this.id = entity.getId();
        this.department = entity.getDepartment();
        this.className = entity.getClassName();

    }
}
