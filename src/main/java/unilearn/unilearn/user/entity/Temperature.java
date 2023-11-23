package unilearn.unilearn.user.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity @Getter@Setter @Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC) @AllArgsConstructor
@Transactional
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temperature_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private double temperature = 36.5;
}
