package unilearn.unilearn.subject.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// 개설 과목 엔티티
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String department; // 개설학과

    private String className; // 수업명

    private String professor; // 교수님

    private int year; // 연도

    private int semester; // 학기
}

