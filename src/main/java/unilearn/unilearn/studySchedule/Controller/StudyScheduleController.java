package unilearn.unilearn.studySchedule.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import unilearn.unilearn.Schedule.dto.ScheduleResponseDto;
import unilearn.unilearn.Schedule.entity.Schedule;
import unilearn.unilearn.Schedule.repository.ScheduleRepository;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.study.repository.StudyRepository;
import unilearn.unilearn.studySchedule.Repository.StudyScheduleRepository;
import unilearn.unilearn.studySchedule.Service.StudyScheduleService;
import unilearn.unilearn.studySchedule.dto.MonthDto;
import unilearn.unilearn.studySchedule.dto.OnedayDto;
import unilearn.unilearn.studySchedule.dto.StudySchedulePostDto;
import unilearn.unilearn.studySchedule.entity.StudySchedule;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/study-schedule")
public class StudyScheduleController {
    @Autowired
    private StudyScheduleService studyScheduleService;
    private final UserRepository userRepository;
    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyRepository studyRepository;
    private final ScheduleRepository scheduleRepository;
    //스터디 일정 추가
    @Transactional
    @PostMapping("/{study_id}")
    public ResponseEntity<Long> addSchedule(@PathVariable("study_id") Long study_id, @RequestBody StudySchedulePostDto dto, Principal principal)  {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        StudySchedule studySchedule = new StudySchedule();
        Study study = studyRepository.findById(study_id)
                .orElseThrow(() -> new RuntimeException("Study not found with id: " + study_id));
        studySchedule.setStudy(study);
        studySchedule.setUser(user);
        studySchedule.setContent(dto.getContent());
        studySchedule.setDeadline(dto.getDeadline());
        studyScheduleRepository.save(studySchedule);
        //스케줄에도 추가
        Schedule schedule = new Schedule();
        schedule.setStudySchedule(studySchedule);
        schedule.setUser(user);
        schedule.setContent(dto.getContent());
        schedule.setDeadline(dto.getDeadline());
        schedule.setChecked(false);
        scheduleRepository.save(schedule);
        return new ResponseEntity<>(studySchedule.getId(), HttpStatus.CREATED);
    }
    //특정 일자의 스터디 리스트 조회
    @GetMapping("/daily-schedule")
    public ResponseEntity<List<OnedayDto>> oneDayList(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate deadline, Principal principal){
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        List<OnedayDto> responseFormList = studyScheduleService.onedayList(deadline, principal);
        return ResponseEntity.status(HttpStatus.OK).body(responseFormList);
    }

    //월별 캘린더 일정 개수 조회
    @GetMapping("/monthly-schedule-count")
    public ResponseEntity<MonthDto> monthlyCount(
            @RequestParam("year")int yearNumber, @RequestParam("month")int monthNumber,
            Principal principal) {
        if (principal.getName() != null) {
            System.out.println(principal.getName() + principal);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByNickname(principal.getName());
        log.info("principal user = " + user.toString());
        MonthDto responseDto = studyScheduleService.monthlyCount(yearNumber, monthNumber, principal);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}

