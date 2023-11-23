package unilearn.unilearn.Schedule.dto;

import lombok.Getter;

import java.time.LocalDate;

public class ScheduleRequestDto {
    @Getter
    public static class ScheduleCreateDto {
        private LocalDate date;
        private String schedule_name;
    }

}
