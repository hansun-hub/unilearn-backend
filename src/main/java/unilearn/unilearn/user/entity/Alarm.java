package unilearn.unilearn.user.entity;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import unilearn.unilearn.global.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity @Getter @Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC) @AllArgsConstructor
@Transactional
public class Alarm extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    private String context;
}
