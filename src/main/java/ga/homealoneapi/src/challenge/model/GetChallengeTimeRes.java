package ga.homealoneapi.src.challenge.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetChallengeTimeRes {
    private String startTime;
    private String endTime;
    private int challengeIdx;

    public GetChallengeTimeRes(String startTime, String endTime, int challengeIdx) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.challengeIdx = challengeIdx;
    }
}
