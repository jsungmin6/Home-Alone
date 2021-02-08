package ga.homealoneapi.src.user.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PostLoginReq {
  private String userName;
  private String userEmail;
  private int snsId;
  private LoginType loginType;
  private  double latitude;
  private  double longitude;
  private  String parcelAddressing;
  private  String streetAddressing;
}
