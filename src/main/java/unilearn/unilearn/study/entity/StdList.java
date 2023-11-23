package unilearn.unilearn.study.entity;
import lombok.*;

import unilearn.unilearn.study.entity.Study;
import unilearn.unilearn.subject.entity.Subject;
import unilearn.unilearn.user.entity.User;

import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Transactional
public class StdList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long std_List_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

}
