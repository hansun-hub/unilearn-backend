package unilearn.unilearn.studySchedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter@Setter
public class StudySchedulePostDto {
    private LocalDate deadline;
    private String content;
}
