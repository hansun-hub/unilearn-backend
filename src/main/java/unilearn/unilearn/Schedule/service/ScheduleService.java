package unilearn.unilearn.Schedule.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.MemberRemoval;
import org.springframework.stereotype.Service;
import unilearn.unilearn.Schedule.dto.ScheduleResponseDto;
import unilearn.unilearn.Schedule.entity.Schedule;
import unilearn.unilearn.Schedule.repository.ScheduleRepository;
import unilearn.unilearn.user.entity.User;
import unilearn.unilearn.user.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    public ScheduleResponseDto.TodayScheduleCountDto todayCount(Principal principal) {
        User user = userRepository.findByNickname(principal.getName());
        LocalDate today = LocalDate.now();

        List<Schedule> scheduleList = scheduleRepository.findByUserAndDeadline(user, today);

        return ScheduleResponseDto.TodayScheduleCountDto.builder()
                .schedule_count(scheduleList.size())
                .date(today)
                .build();
    }

    private ScheduleResponseDto.OneDayScheduleResponseDto toOneDayScheduleResponseDto (Schedule schedule) {
        return ScheduleResponseDto.OneDayScheduleResponseDto.builder()
                .schedule_id(schedule.getId())
                .schedule_name(schedule.getContent())
                .schedule_completed(schedule.isChecked())
                .deadline(schedule.getDeadline())
                .build();
    }

    public List<ScheduleResponseDto.OneDayScheduleResponseDto> onedayList(LocalDate deadline, Principal principal) {
        User user = userRepository.findByNickname(principal.getName());
        List<Schedule> scheduleList = scheduleRepository.findByUserAndDeadline(user, deadline);
        return scheduleList.stream()
                .map(schedule -> toOneDayScheduleResponseDto(schedule))
                .collect(Collectors.toList());
    }
}
