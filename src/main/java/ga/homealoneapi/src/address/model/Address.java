package ga.homealoneapi.src.address.model;

import ga.homealoneapi.config.BaseEntity;
import ga.homealoneapi.config.DeleteStatus;
import ga.homealoneapi.src.user.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "Address") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressIdx")
    private int id;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "parcelAddressing")
    private String parcelAddressing;

    @Column(name = "streetAddressing")
    private String streetAddressing;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userIdx")
    private UserInfo userInfo;

    public Address(double latitude, double longitude, String parcelAddressing, String streetAddressing, DeleteStatus deleteStatus,UserInfo userInfo) {
        this.latitude=latitude;
        this.longitude=longitude;
        this.parcelAddressing=parcelAddressing;
        this.streetAddressing=streetAddressing;
        this.setIsDeleted(deleteStatus);
        this.userInfo=userInfo;

    }
}
