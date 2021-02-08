package ga.homealoneapi.src.user.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Data
public class KakaoRes {
    private final String msg;

    private final int code;

}
