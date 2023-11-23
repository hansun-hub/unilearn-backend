package unilearn.unilearn.update;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import unilearn.unilearn.assignmentsPosts.entity.AssignmentsPosts;
import unilearn.unilearn.studyDetailRepository.AssignmentPostRepository;

import java.util.List;

//자정 before/after갱신

@Component
@EnableScheduling
public class ScheduledTask {

    private final AssignmentPostRepository assignmentsPostsRepository;

    public ScheduledTask(AssignmentPostRepository assignmentsPostsRepository) {
        this.assignmentsPostsRepository = assignmentsPostsRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void updateStatusForAllEntities() {
        List<AssignmentsPosts> allAssignmentsPosts = assignmentsPostsRepository.findAll();

        for (AssignmentsPosts assignmentsPosts : allAssignmentsPosts) {
            assignmentsPosts.updateStatus();
            assignmentsPostsRepository.save(assignmentsPosts);
        }
    }
}