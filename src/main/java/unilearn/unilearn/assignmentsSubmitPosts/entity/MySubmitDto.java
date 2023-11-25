package unilearn.unilearn.assignmentsSubmitPosts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MySubmitDto {

    //이미지, 내용
    String content;
    String img;
}
