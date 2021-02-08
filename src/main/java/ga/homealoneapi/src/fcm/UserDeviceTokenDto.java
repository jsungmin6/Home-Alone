package ga.homealoneapi.src.fcm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class UserDeviceTokenDto {
    private String userDeviceToken;

    public UserDeviceTokenDto(String userDeviceToken) {
        this.userDeviceToken = userDeviceToken;
    }
}
