package unilearn.unilearn.studySchedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthDto {

    private int year;
    private int month;
    private int[] scheduleCount;

}
