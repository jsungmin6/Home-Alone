package ga.homealoneapi.src.user;

import ga.homealoneapi.config.DeleteStatus;
import ga.homealoneapi.src.address.AddressRepository;
import ga.homealoneapi.src.address.model.Address;
import ga.homealoneapi.src.user.models.*;
import ga.homealoneapi.utils.JwtService;
import ga.homealoneapi.config.secret.Secret;
import ga.homealoneapi.utils.AES128;
import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponseStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoProvider userInfoProvider;
    private final JwtService jwtService;
    private  final AddressRepository addressRepository;


    /**
     * 카카오 로그인
     * accessToken을 받고, jwt로 return
     */

    //변경전 userName 뒤에 snsId
    public PostKakaoLoginRes createUserJwt(int userId,String userName) throws BaseException{

        // PostKakaoLoginRes post=new PostKakaoLoginRes(SnsId);
        String jwt=null;
        //변경전 userName뒤 snsId 포함
        jwt=jwtService.createJwt(userId,userName); //토큰 만

        return new PostKakaoLoginRes(userId,jwt);


    }
    /**
     * 회원가입
     */

    @Transactional
    public PostUserRes createUserInfo(PostUserReq postUserReq)throws BaseException{
        UserInfo existsUserInfo=null;
        try{
            //db에 존재하는 회원이 있는지 조
            existsUserInfo=userInfoProvider.PostRetrieveUserInfo(postUserReq.getSnsId());

        }catch (BaseException exception){
            if(exception.getStatus()!=BaseResponseStatus.NOT_FOUND_USER){
                throw exception;
            }


        }
        if(existsUserInfo!=null){
            throw new BaseException(BaseResponseStatus.DUPLICATED_USER);
        }

        // 유저 정보 생성
        String userName=postUserReq.getUserName();
        String userEmail=null;
        if(postUserReq.getUserEmail()!=null){
           userEmail=postUserReq.getUserEmail();
        }
        //변경 전 integer
        String snsId=postUserReq.getSnsId();
        LoginType loginType=postUserReq.getLoginType();
        double latitude= postUserReq.getLatitude();
        double longitude=postUserReq.getLongitude();
        String  parcelAddressing=postUserReq.getParcelAddressing();
        String  streetAddressing=postUserReq.getStreetAddressing();
        ChallengeStatus challengeStatus=ChallengeStatus.N;
        PushStatus pushStatus=PushStatus.N;
        DeleteStatus deleteStatus=DeleteStatus.N;

        UserInfo userInfo=new UserInfo(deleteStatus,userEmail,challengeStatus,pushStatus,snsId,userName,loginType,latitude,longitude,parcelAddressing,streetAddressing);



        try {
                userInfoRepository.save(userInfo);
        }catch (Exception exception){
            throw  new BaseException(BaseResponseStatus.FAILED_TO_POST_USER);
        }

        //주소 엔티티 저장
        DeleteStatus addressDeleteStatus=DeleteStatus.N;
        Address address=new Address(latitude,longitude,parcelAddressing,streetAddressing,addressDeleteStatus,userInfo);
        addressRepository.save(address);


        System.out.println("userId"+userInfo.getId());

        //jwt생성
        String jwt=jwtService.createJwt(userInfo.getId(),userInfo.getUserName());
        return  new PostUserRes(jwt,userInfo.getId());
    }


    @Transactional
    public PatchUserRes updateUserInfo(@NonNull Integer userIdx, PatchUserReq patchUserReq) throws BaseException{
        try{

            UserInfo userInfo=null;
            userInfo=  userInfoRepository.updateByUserIdx(userIdx,patchUserReq);


            return new PatchUserRes(userInfo.getId());


        }catch (Exception ignored){

            throw new BaseException(BaseResponseStatus.FAILED_TO_PATCH_USER);
        }


    }

    @Transactional
    public void createUserDeviceToken(int userIdx, String userDeviceToken) throws BaseException{
        System.out.println("createUserDeviceToken start");
        UserInfo userInfo;

        try{
            userInfo = userInfoRepository.findByUserIdx(userIdx);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        userInfo.setUserDeviceToken(userDeviceToken);

    }
}

//
//    @Autowired
//    public UserInfoService(UserInfoRepository userInfoRepository, UserInfoProvider userInfoProvider, JwtService jwtService) {
//        this.userInfoRepository = userInfoRepository;
//        this.userInfoProvider = userInfoProvider;
//        this.jwtService = jwtService;
//    }
//
//    /**
//     * 회원가입
//     * @param postUserReq
//     * @return PostUserRes
//     * @throws BaseException
//     */
//    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
//        UserInfo existsUserInfo = null;
//        try {
//            // 1-1. 이미 존재하는 회원이 있는지 조회
//            existsUserInfo = userInfoProvider.retrieveUserInfoByEmail(postUserReq.getEmail());
//        } catch (BaseException exception) {
//            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
//            if (exception.getStatus() != BaseResponseStatus.NOT_FOUND_USER) {
//                throw exception;
//            }
//        }
//        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
//        if (existsUserInfo != null) {
//            throw new BaseException(BaseResponseStatus.DUPLICATED_USER);
//        }
//
//        // 2. 유저 정보 생성
//        String email = postUserReq.getEmail();
//        String nickname = postUserReq.getNickname();
//        String phoneNumber = postUserReq.getPhoneNumber();
//        String password;
//        try {
//            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
//        } catch (Exception ignored) {
//            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_USER);
//        }
//        UserInfo userInfo = new UserInfo(email, password, nickname, phoneNumber);
//
//        // 3. 유저 정보 저장
//        try {
//            userInfo = userInfoRepository.save(userInfo);
//        } catch (Exception exception) {
//            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_USER);
//        }
//
//        // 4. JWT 생성
//        String jwt = jwtService.createJwt(userInfo.getId());
//
//        // 5. UserInfoLoginRes로 변환하여 return
//        int id = userInfo.getId();
//        return new PostUserRes(id, jwt);
//    }
//
//    /**
//     * 회원 정보 수정 (POST uri 가 겹쳤을때의 예시 용도)
//     * @param patchUserReq
//     * @return PatchUserRes
//     * @throws BaseException
//     */
//    public PatchUserRes updateUserInfo(@NonNull Integer userId, PatchUserReq patchUserReq) throws BaseException {
//        try {
//            String email = patchUserReq.getEmail().concat("_edited");
//            String nickname = patchUserReq.getNickname().concat("_edited");
//            String phoneNumber = patchUserReq.getPhoneNumber().concat("_edited");
//            return new PatchUserRes(email, nickname, phoneNumber);
//        } catch (Exception ignored) {
//            throw new BaseException(BaseResponseStatus.FAILED_TO_PATCH_USER);
//        }
//    }
//
//    /**
//     * 회원 탈퇴
//     * @param userId
//     * @throws BaseException
//     */
//    public void deleteUserInfo(int userId) throws BaseException {
//        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
//        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserId(userId);
//
//        // 2-1. 해당 UserInfo를 완전히 삭제
////        try {
////            userInfoRepository.delete(userInfo);
////        } catch (Exception exception) {
////            throw new BaseException(DATABASE_ERROR_USER_INFO);
////        }
//
//        // 2-2. 해당 UserInfo의 status를 INACTIVE로 설정
//        userInfo.setStatus("INACTIVE");
//        try {
//            userInfoRepository.save(userInfo);
//        } catch (Exception ignored) {
//            throw new BaseException(BaseResponseStatus.FAILED_TO_DELETE_USER);
//        }
//    }
//}