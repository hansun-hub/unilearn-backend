package unilearn.unilearn.subject.entity;

import lombok.*;
import unilearn.unilearn.global.entity.BaseTimeEntity;

import javax.persistence.*;

// 과제 댓글 엔티티
@Entity
@Table(name="assignment_comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AssignmentComment extends BaseTimeEntity {

    @Id @Column(name="assignment_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment; // 과제

    private String comment; // 댓글 내용
}
