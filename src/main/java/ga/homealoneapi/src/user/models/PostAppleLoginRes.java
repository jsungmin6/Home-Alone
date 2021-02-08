package ga.homealoneapi.src.user.models;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostAppleLoginRes {

    private final int userId;
    private final String jwt;
}
