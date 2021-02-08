package ga.homealoneapi.src.challenge;

import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponse;
import ga.homealoneapi.config.BaseResponseStatus;
import ga.homealoneapi.config.DeleteStatus;
import ga.homealoneapi.src.address.AddressRepository;
import ga.homealoneapi.src.address.model.Address;
import ga.homealoneapi.src.address.model.GetRecentlyLocationRes;
import ga.homealoneapi.src.challenge.model.*;
import ga.homealoneapi.src.user.UserInfoRepository;
import ga.homealoneapi.src.user.models.ChallengeStatus;
import ga.homealoneapi.src.user.models.UserInfo;
import ga.homealoneapi.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtService jwtService;

    /**
     * 회원 시간 조회
     * @param userIdx
     * @return List<GetRecentlyLocationRes>
     * @throws BaseException
     */
    public GetChallengeTimeRes getChallengeTime(int userIdx) throws BaseException{

        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findByUserIdx(userIdx);
            System.out.println("challengeStatus: "+userInfo.getChallengeStatus());
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        ChallengeStatus challengeStatus = userInfo.getChallengeStatus();
        if(challengeStatus.equals(ChallengeStatus.N)){
            throw new BaseException(BaseResponseStatus.FAILED_TO_CHALLENGE_TIME);
        }

        Challenge challenges;
        challenges = challengeRepository.findRecentlyById(userIdx);


        String start = challenges.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = challenges.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        GetChallengeTimeRes result = new GetChallengeTimeRes(start,end,challenges.getId());
        return result;

    }

    /**
     * 챌린지 등록
     * @param parameters, userIdx
     * @return PostChallengeTimeRes
     * @throws BaseException
     */
    public PostChallengeTimeRes createChallengeTime(PostChallengeTimeReq parameters, int userIdx) throws BaseException {

        //존재하는 유저인지
        //challengeStatus가 현재 Y인지

        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findByUserIdx(userIdx);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        ChallengeStatus challengeStatus = userInfo.getChallengeStatus();
        if(challengeStatus.equals(ChallengeStatus.Y)){
            throw new BaseException(BaseResponseStatus.FAILED_TO_ALREADY_START);
        }

        //challenge에 insert
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(parameters.getStartTime(), formatter);
        LocalDateTime end = LocalDateTime.parse(parameters.getEndTime(), formatter);

        Challenge challenge = new Challenge();
        challenge.setIsDeleted(DeleteStatus.N);
        challenge.setSuccessStatus(SuccessStatus.N);
        challenge.setStartTime(start);
        challenge.setEndTime(end);
        challenge.setUserInfo(userInfo);
        int challengeIdx = challengeRepository.save(challenge);


        //userInfo에 ChallengeStatus Y->N
        userInfo.setChallengeStatus(ChallengeStatus.Y);

        return new PostChallengeTimeRes(challengeIdx);
    }

    /**
     * 챌린지 성공
     * @param challengeIdx
     * @return boolean
     * @throws BaseException
     */
    public void successChallenge(Integer challengeIdx,int userIdx) throws BaseException {
        //존재하는 유저인지
        //challengeStatus가 현재 Y인지

        if (challengeIdx == null || challengeIdx <= 0) {
            throw new BaseException(BaseResponseStatus.EMPTY_USERID);
        }

        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findByUserIdx(userIdx);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        Challenge challenge;
        try{
            challenge = challengeRepository.findById(challengeIdx);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_CHALLENGE);
        }

        if (challenge.getSuccessStatus().equals(SuccessStatus.Y)){
            throw new BaseException(BaseResponseStatus.FAILED_TO_ALREADY_SUCCESS);
        }

        Challenge recentlyById = challengeRepository.findRecentlyById(userIdx);
        System.out.println("recentlyChallengeIdx : "+recentlyById.getId());
        System.out.println("parameter challengeIdx : "+challengeIdx);
        if (recentlyById.getId() != challengeIdx){
            throw new BaseException(BaseResponseStatus.FAILED_TO_MATCH_CHALLENGE);
        }

        challenge.setSuccessStatus(SuccessStatus.Y);
        userInfo.setChallengeStatus(ChallengeStatus.N);

    }

    public void failChallenge(Integer challengeIdx, int userIdx) throws BaseException {
        //존재하는 유저인지
        //challengeStatus가 현재 Y인지
        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findByUserIdx(userIdx);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        Challenge challenge;
        try{
            challenge = challengeRepository.findById(challengeIdx);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_CHALLENGE);
        }

        if (challenge.getSuccessStatus().equals(SuccessStatus.Y)){
            throw new BaseException(BaseResponseStatus.FAILED_TO_ALREADY_SUCCESS);
        }

        Challenge recentlyById = challengeRepository.findRecentlyById(userIdx);
        System.out.println("recentlyChallengeIdx : "+recentlyById.getId());
        System.out.println("parameter challengeIdx : "+challengeIdx);
        if (recentlyById.getId() != challengeIdx){
            throw new BaseException(BaseResponseStatus.FAILED_TO_MATCH_CHALLENGE);
        }

        userInfo.setChallengeStatus(ChallengeStatus.N);
    }
}
