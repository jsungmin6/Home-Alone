package ga.homealoneapi.src.challenge;

import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponse;
import ga.homealoneapi.config.BaseResponseStatus;
import ga.homealoneapi.src.address.model.PostUserLocationReq;
import ga.homealoneapi.src.challenge.model.GetChallengeTimeRes;
import ga.homealoneapi.src.challenge.model.PostChallengeTimeReq;
import ga.homealoneapi.src.challenge.model.PostChallengeTimeRes;
import ga.homealoneapi.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final JwtService jwtService;
    private final ChallengeService challengeService;
    /**
     * 회원 시간 조회 API
     * [GET] /users/challenge-times
     * @return BaseResponse<GetChallengeTimeRes>
     */
    @ResponseBody
    @GetMapping("/users/challenge-times")
    public BaseResponse<GetChallengeTimeRes> getChallengeTime() {
        System.out.println("getChallengeTime start");
        //jwt 토큰 에서 userIdx 얻기
        int userIdx;
        try {
            userIdx = jwtService.getUserId();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        System.out.println("userIdx :"+userIdx);

        try {
            GetChallengeTimeRes challengeTime = challengeService.getChallengeTime(userIdx);//TODO: jwt에서 받은 userIdx 넣어줘야함.
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, challengeTime);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 시간 등록 API
     * [POST] /users/challenge-times
     * @RequestBody PostChallengeTimeReq
     * @return BaseResponse<PostChallengeTimeRes>
     */
    @ResponseBody
    @PostMapping("/users/challenge-times")
    public BaseResponse<PostChallengeTimeRes> postChallengeTime(@RequestBody PostChallengeTimeReq parameters) {
        System.out.println("postChallengeTime start");
        if (parameters.getStartTime() == null) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_STARTTIME);
        }

        if (parameters.getEndTime() == null) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_ENDTIME);
        }

        //jwt 토큰 에서 userIdx 얻기
        int userIdx;
        try {
            userIdx = jwtService.getUserId();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        System.out.println("userIdx :"+userIdx);

        try {
            PostChallengeTimeRes challengeTime = challengeService.createChallengeTime(parameters, userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS,challengeTime);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 챌린지 성공 API
     * [POST] /challenges/:challengeIdx/success
     * @PathVariable challengeIdx
     * @return BaseResponse<PostChallengeTimeRes>
     */
    @ResponseBody
    @PostMapping("/challenges/{challengeIdx}/success")
    public BaseResponse<Void> postSuccessChallenge(@PathVariable Integer challengeIdx) {
        System.out.println("postSuccessChallenge start");

        if (challengeIdx == null || challengeIdx <= 0) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
        }

        //jwt 토큰 에서 userIdx 얻기
        int userIdx;
        try {
            userIdx = jwtService.getUserId();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        System.out.println("userIdx :"+userIdx);

        try {
            challengeService.successChallenge(challengeIdx,userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 챌린지 실패 API
     * [POST] /challenges/:challengeIdx/fail
     * @PathVariable challengeIdx
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PostMapping("/challenges/{challengeIdx}/fail")
    public BaseResponse<Void> postFailChallenge(@PathVariable Integer challengeIdx) {
        System.out.println("postFailChallenge start");
        //jwt 토큰 에서 userIdx 얻기
        int userIdx;
        try {
            userIdx = jwtService.getUserId();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        System.out.println("userIdx :"+userIdx);

        try {
            challengeService.failChallenge(challengeIdx,userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
