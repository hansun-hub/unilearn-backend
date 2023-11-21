package unilearn.unilearn.user.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity @Getter @Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC) @AllArgsConstructor
@Transactional
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temperature_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ColumnDefault("36.5")
    private double temperature;
}
