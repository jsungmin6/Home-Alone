package ga.homealoneapi.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLoginRes {

    private final String jwt;
}
