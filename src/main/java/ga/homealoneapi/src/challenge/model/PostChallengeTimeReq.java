package ga.homealoneapi.src.challenge.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostChallengeTimeReq {

    private String startTime;
    private String endTime;

}
