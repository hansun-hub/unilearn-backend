package unilearn.unilearn.assignmentsSubmitPosts.entity;

import java.time.LocalDateTime;

public class AssignmentsSubmitPostsDto {

    private String title;
    private String content;
    private LocalDateTime deadline;

    // 기본 생성자
    public AssignmentsSubmitPostsDto() {

    }


    public AssignmentsSubmitPostsDto(String title, String content, LocalDateTime deadline) {

        this.content = content;
    }

    // getter 및 setter 메서드

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




    // toString 메서드 (디버깅 및 로깅용)
    @Override
    public String toString() {
        return "AssignmentsSubmitPostsDto{" +
                ", content='" + content + '\'' +
                '}';
    }


}
