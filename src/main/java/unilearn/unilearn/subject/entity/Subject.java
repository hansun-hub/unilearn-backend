package unilearn.unilearn.subject.entity;

import lombok.Getter;

import javax.persistence.*;

// 개설 과목 엔티티
@Getter
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String department; // 개설학과

    private String className; // 수업명

    private String professor; // 교수님
    @Column(name = "`year`")//안 하면 sql문 에러나서 수정했습니다. - 서윤
    private int year; // 연도

    private int semester; // 학기

}

