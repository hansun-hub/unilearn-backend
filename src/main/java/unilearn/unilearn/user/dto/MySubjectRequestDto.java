package unilearn.unilearn.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MySubjectRequestDto {
    @Getter
    public static class MySubjectRequestForm {
        @NotEmpty
        private String department;
        @NotNull
        private int year;
        @NotEmpty
        private String semester;
        @NotEmpty
        private String professor;
        @NotEmpty
        private String sbj_name;
    }
}
