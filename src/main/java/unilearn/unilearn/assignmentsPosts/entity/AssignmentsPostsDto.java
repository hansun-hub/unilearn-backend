package unilearn.unilearn.assignmentsPosts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class AssignmentsPostsDto {

    private String title;
    private String content;
    private LocalDateTime deadline;
    private String status;

    // 기본 생성자
    public AssignmentsPostsDto() {

    }

    
    public AssignmentsPostsDto(String title, String content, LocalDateTime deadline, String status) {

        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.status = status;
    }

    // getter 및 setter 메서드


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        LocalDateTime currentTime = LocalDateTime.now();

        if(currentTime.isBefore(deadline) || currentTime.isEqual(deadline)) // 같거나 전이면 진행중
            this.status = "before";
        else
            this.status ="after";  // 이후이면 마감
    }
    // toString 메서드 (디버깅 및 로깅용)
    @Override
    public String toString() {
        return "AssignmentPostDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", deadline=" + deadline +
                '}';
    }




}

