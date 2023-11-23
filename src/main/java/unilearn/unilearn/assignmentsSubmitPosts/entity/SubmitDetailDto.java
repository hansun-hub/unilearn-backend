package unilearn.unilearn.assignmentsSubmitPosts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitDetailDto {
    Long submit_post_id;
    //스터디명,과제제목,제출일시,제출자,과제내용,이미지
    //String study_name;
    String assignment_name;
    LocalDateTime submit_time;
    String name;
    String content;
    //이미지


}
