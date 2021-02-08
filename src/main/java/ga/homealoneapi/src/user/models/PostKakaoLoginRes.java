package ga.homealoneapi.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostKakaoLoginRes {

    private final  int userId;
    private final String jwt;
}
