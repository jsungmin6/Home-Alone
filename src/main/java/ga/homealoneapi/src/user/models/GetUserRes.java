package ga.homealoneapi.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserRes {
//    private final int userId;
//    private final String email;
//    private final String nickname;
//    private final String phoneNumber;

//
//    private final  String userName;
//    private final  String addressName;
//    private final PushStatus pushStatus;

        private final  int userId;
        private final  String userName;
        private final  PushStatus pushStatus;
        private final  ChallengeStatus challengeStatus;
        private final LoginType loginType;
}