package unilearn.unilearn.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class MySubjectResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MySubjectResponseForm {
        private Long MySubjectId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MySubjectResponseListForm {
        private Long courseId;
        private String department;
        private int year;
        private String semester;
        private String professor;
        private String sbj_name;
    }
}
