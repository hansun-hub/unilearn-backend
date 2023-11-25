package unilearn.unilearn.subject.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentForm {

    private Long id; //과제게시글 id

    private Long author;

    //private Long subject;
    private String content;



}
