package unilearn.unilearn.assignmentsSubmitPosts.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import unilearn.unilearn.global.config.S3Uploader;

import java.io.IOException;
import java.time.LocalDateTime;
@Getter@Setter
public class AssignmentsSubmitPostsDto {
    private String img;
    private String title;
    private String content;
    private LocalDateTime deadline;
    private  S3Uploader s3Uploader;

    // 기본 생성자
    public AssignmentsSubmitPostsDto() {

    }


    public AssignmentsSubmitPostsDto(String img, String title, String content, LocalDateTime deadline) {

        this.content = content;
    }

    // getter 및 setter 메서드

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getImg(){ return img;}

    public  void setImg(MultipartFile multipartFile)throws IOException {
        String dirName = "assignnmentsSubmitPosts/submitimg";
        this.img = s3Uploader.upload(multipartFile,dirName);}


    // toString 메서드 (디버깅 및 로깅용)
    @Override
    public String toString() {
        return "AssignmentsSubmitPostsDto{" +
                ", content='" + content + '\'' +
                '}';
    }


}
