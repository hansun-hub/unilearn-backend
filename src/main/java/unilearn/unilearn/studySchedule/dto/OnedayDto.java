package unilearn.unilearn.studySchedule.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnedayDto {
    private Long study_schedule_id;
    private String schedule_name;
    private LocalDate deadline;
}
