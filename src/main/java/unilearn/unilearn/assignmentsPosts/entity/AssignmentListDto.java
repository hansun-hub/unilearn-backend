package unilearn.unilearn.assignmentsPosts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentListDto {
    //id, 제목, 내용, 마감기한, 제출일자, 제출자들
    private Long assignment_id;
    private String title;
    private LocalDateTime deadline;
    private  Long submit_num;
    private String submit_name;



}


