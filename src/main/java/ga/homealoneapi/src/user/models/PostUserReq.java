package ga.homealoneapi.src.user.models;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {

    private String userName;
    private String userEmail;
    private String snsId;
    private LoginType loginType;
    private  double latitude;
    private  double longitude;
    private  String parcelAddressing;
    private  String streetAddressing;
}
