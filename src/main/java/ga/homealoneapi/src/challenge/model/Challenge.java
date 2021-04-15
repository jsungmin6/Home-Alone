package ga.homealoneapi.src.challenge.model;

import ga.homealoneapi.config.BaseEntity;
import ga.homealoneapi.src.user.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Data
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "Challenge") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Challenge extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challengeIdx")
    private int id;

    @Column(name = "startTime")
    private LocalDateTime startTime;

    @Column(name = "endTime")
    private LocalDateTime endTime;


    @Column(name = "successStatus")
    @Enumerated(EnumType.STRING)
    private SuccessStatus successStatus;

    @Column(name = "certification")
    private int certification;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userIdx")
    private UserInfo userInfo;

}
