package ga.homealoneapi.src.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PatchUserReq {

    private PushStatus pushStatus;
    private double  latitude;
    //private double longtitude;
    private  double longitude;
    private String parcelAddressing;
    private String streetAddressing;



}
