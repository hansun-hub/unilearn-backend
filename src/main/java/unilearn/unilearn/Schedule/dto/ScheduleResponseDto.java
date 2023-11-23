package unilearn.unilearn.Schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ScheduleResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodayScheduleCountDto {
        private LocalDate date;
        private int schedule_count;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OneDayScheduleResponseDto {
        private Long schedule_id;
        private String schedule_name;
        private boolean schedule_completed;
        private LocalDate deadline;
    }
}
