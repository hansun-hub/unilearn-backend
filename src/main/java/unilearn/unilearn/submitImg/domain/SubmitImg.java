package unilearn.unilearn.submitImg.domain;

import lombok.*;
import unilearn.unilearn.assignmentsSubmitPosts.domain.AssignmentsSubmitPosts;

import javax.persistence.*;

@Entity
@Table(name="subimg")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubmitImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="submit_img_id")
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignments_submit_posts_id")
    private AssignmentsSubmitPosts assignmentsSubmitPosts;
}
