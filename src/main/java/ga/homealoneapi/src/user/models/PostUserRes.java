package ga.homealoneapi.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserRes {

    private final String jwt;
    private  final int userIdx;
}
