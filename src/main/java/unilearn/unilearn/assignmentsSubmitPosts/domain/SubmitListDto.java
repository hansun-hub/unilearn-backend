package unilearn.unilearn.assignmentsSubmitPosts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitListDto {
    //id,제출자명, 제출일시, 점수, 피드백
    Long submit_post_id;
    String name;
    LocalDateTime submit_time;
    int total_score;
    String feedback;

}
