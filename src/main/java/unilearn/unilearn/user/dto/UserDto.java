package unilearn.unilearn.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponseDto {
        private Long user_id;
        private String nickname;
        private String major;
        private String student_id;
        private String introduction;
        private String school;
        private double temperature;
    }

    @Getter
    public static class UserRequestDto {
        private String introduction;
    }

    @Getter
    public static class UserAuthRequestDto {
        private String student_id;
        private String major;
    }
}
