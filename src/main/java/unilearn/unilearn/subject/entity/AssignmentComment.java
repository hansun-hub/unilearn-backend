package unilearn.unilearn.subject.entity;

import javax.persistence.*;

// 과제 댓글 엔티티
@Entity
public class AssignmentComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment; // 과제

    private String comment; // 댓글 내용
}
