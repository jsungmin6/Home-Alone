package ga.homealoneapi.src.user.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Data
public class postUserDeviceTokenReq {
    private String userDeviceToken;
}
