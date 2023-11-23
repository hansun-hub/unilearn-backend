package unilearn.unilearn.Schedule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unilearn.unilearn.Schedule.dto.ScheduleResponseDto;
import unilearn.unilearn.Schedule.service.ScheduleService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/todays-schedule-count")
    public ResponseEntity<ScheduleResponseDto.TodayScheduleCountDto> todayCount(Principal principal){
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ScheduleResponseDto.TodayScheduleCountDto responseForm = scheduleService.todayCount(principal);
        return ResponseEntity.status(HttpStatus.OK).body(responseForm);
    }

    @GetMapping("/daily-schedule")
    public ResponseEntity<List<ScheduleResponseDto.OneDayScheduleResponseDto>> oneDayList(
            @RequestParam("date") LocalDate deadline, Principal principal){
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ScheduleResponseDto.OneDayScheduleResponseDto> responseFormList = scheduleService.onedayList(deadline, principal);
        return ResponseEntity.status(HttpStatus.OK).body(responseFormList);
    }
}
