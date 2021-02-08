package ga.homealoneapi.src.user.models;

import ga.homealoneapi.config.BaseEntity;
import ga.homealoneapi.config.DeleteStatus;
import lombok.*;

import javax.persistence.*;

//@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
//@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class UserInfo extends BaseEntity {
    /**
     * 유저 ID
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "userIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 이메일
     */
    @Column(name = "userEmail")
    private String email;

    /**
     * 도전 여부
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "challengeStatus")
    private ChallengeStatus challengeStatus;

    /**
     * 푸쉬알람
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "pushStatus")
    private PushStatus pushStatus;

    /**
     * snsId
     */
    @Column(name = "snsId")
    private String snsId;

    /**
     * 이름
     */
    @Column(name = "userName")
    private String userName;

    /**
     * 로그인 타입
     */
    @Column(name = "loginType")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "parcelAddressing")
    private String parcelAddressing;

    @Column(name = "streetAddressing")
    private String streetAddressing;

    @Column(name = "userDeviceToken")
    private String userDeviceToken;

    public UserInfo(DeleteStatus deleteStatus, String userEmail, ChallengeStatus challengeStatus, PushStatus pushStatus, String snsId, String userName, LoginType loginType, double latitude, double longitude, String parcelAddressing, String streetAddressing){
        this.setIsDeleted(deleteStatus);
        this.email=userEmail;
        this.challengeStatus=challengeStatus;
        this.pushStatus=pushStatus;
        this.snsId=snsId;
        this.userName=userName;
        this.loginType=loginType;
        this.latitude=latitude;
        this.longitude=longitude;
        this.parcelAddressing=parcelAddressing;
        this.streetAddressing=streetAddressing;
    }


    public UserInfo(PushStatus pushStatus, String parcelAddressing, double latitude, double longtitude, String streetAddressiong) {
        this.pushStatus=pushStatus;
        this.parcelAddressing=parcelAddressing;
        this.latitude=latitude;
        this.longitude=longtitude;
        this.streetAddressing=streetAddressiong;
    }
}
