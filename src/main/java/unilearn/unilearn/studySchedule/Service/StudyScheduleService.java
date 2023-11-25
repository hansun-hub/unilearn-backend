package unilearn.unilearn.studySchedule.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unilearn.unilearn.Schedule.dto.ScheduleResponseDto;
import unilearn.unilearn.Schedule.entity.Schedule;
import unilearn.unilearn.Schedule.service.ScheduleService;
import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.study.repository.StudyRepository;
import unilearn.unilearn.study.service.StudyService;
import unilearn.unilearn.studySchedule.Repository.StudyScheduleRepository;
import unilearn.unilearn.studySchedule.dto.MonthDto;
import unilearn.unilearn.studySchedule.dto.OnedayDto;
import unilearn.unilearn.studySchedule.entity.StudySchedule;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyScheduleService {

    private final UserRepository userRepository;
    private final StudyScheduleRepository studyScheduleRepository;
    private  final StudyRepository studyRepository;
    public MonthDto monthlyCount(int yearNumber, int monthNumber, Principal principal,Long studyId) {
        User user = userRepository.findByNickname(principal.getName());
        Study study = studyRepository.findById(studyId).orElse(null);
        List<StudySchedule> studySchedules = studyScheduleRepository.findByYearAndMonth(yearNumber, monthNumber, user,study);

        LocalDate startDay = LocalDate.of(yearNumber, monthNumber, 1);
        LocalDate endDay = startDay.withDayOfMonth(startDay.lengthOfMonth());

        int[] scheduleCountArray = calculateScheduleCountArray(startDay, endDay, studySchedules);

        return MonthDto.builder()
                .year(yearNumber)
                .month(monthNumber)
                .scheduleCount(scheduleCountArray)
                .build();
    }

    private int[] calculateScheduleCountArray(LocalDate startDay, LocalDate endDay, List<StudySchedule> scheduleList) {
        int[] scheduleCountArray = new int[endDay.getDayOfMonth()];

        for (int i = 0; i < scheduleCountArray.length; i++) {
            final int dayOfMonth = i + 1;
            long count = scheduleList.stream()
                    .filter(schedule -> schedule.getDeadline().equals(LocalDate.of(endDay.getYear(), endDay.getMonth(), dayOfMonth)))
                    .count();
            scheduleCountArray[i] = (int) count;
        }
        return scheduleCountArray;
    }
    public List<OnedayDto> onedayList(LocalDate deadline, Principal principal,Long studyId) {
        User user = userRepository.findByNickname(principal.getName());
        Study study = studyRepository.findById(studyId).orElse(null);
        List<StudySchedule> scheduleList = studyScheduleRepository.findByUserAndDeadlineAndStudy(user, deadline, study);
        return scheduleList.stream()
                .map(schedule -> toOnedayDto(schedule))
                .collect(Collectors.toList());
    }
    private OnedayDto toOnedayDto (StudySchedule studySchedule) {
        return OnedayDto.builder()
                .study_schedule_id(studySchedule.getId())
                .schedule_name(studySchedule.getContent())
                .deadline(studySchedule.getDeadline())
                .build();
    }

}