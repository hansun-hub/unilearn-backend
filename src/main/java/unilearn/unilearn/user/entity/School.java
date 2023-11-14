package unilearn.unilearn.user.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor @ToString
public class School {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="school_id")
    private Long id;
    private String schoolName;
    private String emailFormat;
}
