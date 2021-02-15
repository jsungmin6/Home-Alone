package ga.homealoneapi.src.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.gson.Gson;
import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponse;
import ga.homealoneapi.config.BaseResponseStatus;
import ga.homealoneapi.config.secret.Secret;
import ga.homealoneapi.src.user.models.*;
import ga.homealoneapi.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.io.FileUtils;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;



//애플로그인 구현시 필요한 파일 임포트

import io.jsonwebtoken.io.Decoders;
import  org.bouncycastle.asn1.pkcs.PrivateKeyInfo ;
import  org.bouncycastle.openssl.PEMParser ;
import  org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter ;

import java.io.FileReader;
import  java.security.PrivateKey ;
import  java.security.PublicKey ;
import  io.jsonwebtoken.JwsHeader ;
import  io.jsonwebtoken.Jwts ;
import  io.jsonwebtoken.SignatureAlgorithm ;
import  java.util.Date ;
import  java.io.File ;
import java.util.concurrent.TimeUnit;

import  org.springframework.util.ResourceUtils ;
import  okhttp3.RequestBody ;
import  org.json.JSONObject ;

import  org.json.JSONObject ;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserInfoController {


    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private  final ResourceLoader resourceLoader;


    private static String APPLE_AUTH_URL = Secret.APPLE_AUTH_URL;
    private final String appleClientId = Secret.APPLE_CLIENT_ID;
    private final String appleKeyId = Secret.APPLE_KEY_ID;
    private final String appleTeamId = Secret.APPLE_TEAM_ID;
    private final String appleKeyPath=Secret.APPLE_KEY_PATH;



    private String generateJWT(String identiferFromApp) throws Exception {
        // Generate a private key for token verification from your end with your creds
        PrivateKey pKey = generatePrivateKey();
        String token = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleKeyId)
                .setIssuer(appleTeamId)
                .setAudience("https://appleid.apple.com")
                .setSubject(identiferFromApp)
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.ES256,pKey)//pkey ,SignatureAlgorithm.ES256 순서였는데 바
                .compact();
        System.out.println(generatePrivateKey());
        return token;
    }

    // Method to generate private key from certificate you created
//    private PrivateKey generatePrivateKey() throws Exception {
//
////        // here i have added cert at resource/apple folder. So if you have added somewhere else, just replace it with your path ofcert
//        File file = ResourceUtils.getFile("classpath:static/AuthKey_79V292KDX7.p8");
//        final PEMParser pemParser = new PEMParser(new FileReader(file));
//        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
//        final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
//        final PrivateKey pKey = converter.getPrivateKey(object);
//        pemParser.close();
//        return pKey;
//    }

    private PrivateKey generatePrivateKey() throws Exception {


        ClassPathResource classPathResource = new ClassPathResource(appleKeyPath);
        InputStream inputStream = classPathResource.getInputStream();
        File file1 = File.createTempFile("AuthKey_79V292KDX7", ".p8");
        System.out.println(file1);

        System.out.println("##################");
        File file2 = File.createTempFile("AuthKey_79V292KDX7", ".p8");
        System.out.println(file2);

        System.out.println("inputStream" + inputStream.toString());
        try {
            FileUtils.copyInputStreamToFile(inputStream, file1);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
            // here i have added cert at resource/apple folder. So if you have added somewhere else, just replace it with your path ofcert
            //File file = ResourceUtils.getFile("classpath:static/AuthKey_79V292KDX7.p8");
            final PEMParser pemParser = new PEMParser(new FileReader(file1));
            final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            final PrivateKey pKey = converter.getPrivateKey(object);
            pemParser.close();
            return pKey;
        }


    /**
     * 카카오톡 로그인 api
     *
     * @param postKakaoLoginReq
     * @return
     * @throws BaseException
     */


    @ResponseBody
    @PostMapping("/login/kakao")
    public BaseResponse<PostKakaoLoginRes> getKakaoUser(@org.springframework.web.bind.annotation.RequestBody PostKakaoLoginReq postKakaoLoginReq) throws BaseException, JsonProcessingException {
        //토큰값을 snsid로 바꿔서 provider에게 보내기
        //postKakaoLoginReq.getAccessToken()


        PostKakaoLoginRes postKakaoLoginRes = null;
        //GetKakaoUserRes getKakaoUserRes;
        RestTemplate rt = new RestTemplate();//http요청을 간단하게 해줄 수 있는 클래

        System.out.println("getAccessToken : "+postKakaoLoginReq.getAccessToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + postKakaoLoginReq.getAccessToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");



        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);


        ResponseEntity<String> response=null;
        try {
            response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoProfileRequest,
                    String.class
            );
        }catch (HttpClientErrorException exception){
            int statusCode=exception.getStatusCode().value();


            //잘못된 형식
            if(statusCode==400){
                return new BaseResponse<>(BaseResponseStatus.INVALID_KAKAO);
            }

            else if(statusCode==401){
                return new BaseResponse<>(BaseResponseStatus.INVALID_KAKAO);
            }




        }


        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile profile = null;
        String jwt = null;
        UserInfo userInfo=null;

        //Model과 다르게 되있으면 그리고 getter setter가 없으면 오류가 날 것이다.
        try {
            profile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
           try {
               userInfo = userInfoProvider.findOne(profile.getId());//snsId 값 넘김


           }catch (BaseException exception){
               return new BaseResponse<>(BaseResponseStatus.NEW_KAKAO_USERS);
           }

        } catch (JsonProcessingException e) {
            e.printStackTrace();


        }


        // 변경전 jwt=jwtService.createJwt(userInfo.getId(), userInfo.getUserName(), userInfo.getSnsId());
        jwt=jwtService.createJwt(userInfo.getId(), userInfo.getUserName());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS_KAKAO_LOGIN, new PostKakaoLoginRes(userInfo.getId(),jwt));
    }



    /**
     *
     * 애플 로그인
     */

    @ResponseBody
    @PostMapping("/login/apple")
    public BaseResponse<PostAppleLoginRes> getAppleUser(@org.springframework.web.bind.annotation.RequestBody SocialParametersDTO socialParametersDTO) throws BaseException,Exception{
        // log.debug("Get Apple User Profile {}", socialParametersDTO);
        System.out.println(socialParametersDTO.getIdentityToken());
        System.out.println(socialParametersDTO.getAuthorizationCode());
        //  System.out.println(socialParametersDTO);
//        String appClientId = null;
//        if (socialParametersDTO.getIdentifierFromApp() != null) {
//            // if kid is sent from mobile app
//            appClientId = socialParametersDTO.getIdentifierFromApp();
//        } else {
        //if doing sign in with web using predefined identifier

        System.out.println(socialParametersDTO.getAuthorizationCode());
        System.out.println(socialParametersDTO.getIdentityToken());
        String appClientId = appleClientId;
        System.out.println("appleClientId" + appClientId);
        //   }
        //SocialUserDTO socialUserDTO = new SocialUserDTO();
        // generate personal verification token
        String token = generateJWT(appClientId);

        System.out.println("token" + "%%%%%%" + token);
        ////////// Get OAuth Token from Apple by exchanging code
        // Prepare client, you can use other Rest client library also
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(70, TimeUnit.SECONDS)
                .writeTimeout(70, TimeUnit.SECONDS)
                .readTimeout(70, TimeUnit.SECONDS)
                .build();
        // Request body for sending parameters as FormUrl Encoded
        String code=socialParametersDTO.getAuthorizationCode();

        okhttp3.RequestBody requestBody = new FormBody
                .Builder()
                .add("client_id", appClientId)
                .add("client_secret", token)
                .add("grant_type", "authorization_code")
                .add("code", socialParametersDTO.getAuthorizationCode())
                .build();


        // Prepare rest request
        Request request = new Request
                .Builder()
                .url("https://appleid.apple.com/auth/token")
                .post(requestBody)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        //System.out.println("request" + requestBody);

        // Execute api call and get Response
        Response resp = okHttpClient.newCall(request).execute();

        String response = resp.body().string();
        // Parse response as DTO
        ObjectMapper objectMapper = new ObjectMapper();
        AppleTokenResponse appleTokenResponse = objectMapper.readValue(response, AppleTokenResponse.class);
        System.out.println(response);//어떤 것으로 틀렸는지 알려주는 코드임 400번대 오류처

        if(resp.code()==400){// 유효자지 않은 토큰일때 오류발
            return new BaseResponse<>(BaseResponseStatus.INVALID_APPLE);
        }
        // Parse id token from Token
        String idToken = appleTokenResponse.getId_token();//사용자의 id정보가 표시된 것

        System.out.println("idToken"+idToken);//사용자의 정보가 포함된 json토큰

        String payload = idToken.split("\\.")[1];// 0 is header we ignore it for now
        String decoded = new String(Decoders.BASE64.decode(payload));
        AppleIDTokenPayload idTokenPayload = new Gson().fromJson(decoded, AppleIDTokenPayload.class);

        String snsId= socialParametersDTO.getUser();
        UserInfo userInfo=null; //기존 회원 정보가 없을경우생
        try {
            userInfo=userInfoProvider.findOne(snsId);


        }catch (BaseException exception){
            return new BaseResponse<>(BaseResponseStatus.NEW_APPLE_USERS);
        }





        //회원 반환값 생성
       String jwt=jwtService.createJwt(userInfo.getId(), userInfo.getUserName());
        PostAppleLoginRes postAppleLoginRes=new PostAppleLoginRes(userInfo.getId(),jwt);


        // if we have user obj also from Web or mobile
        // we get only at 1st authorization
//        if (socialParametersDTO.getUser() != null) {
//            JSONObject user = new JSONObject(socialParametersDTO.getUser());
//            JSONObject name = user.has("name") ? user.getJSONObject("name") : null;
//            String firstName = name.getString("firstName");
//            String lastName = name.getString("lastName");
//
//        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS_APPLE_LOGIN,postAppleLoginRes);
    }


//    @ResponseBody
//    @PostMapping("/login/apple")
//    public BaseResponse<PostAppleLoginRes> getAppleUser(@org.springframework.web.bind.annotation.RequestBody SocialParametersDTO socialParametersDTO) throws BaseException,Exception{
//        UserInfo userInfo=null; //기존 회원 정보가 없을경우생
//        try {
//            userInfo=userInfoProvider.findOne("001407.2a3e82da16464cfdad01fcde812d7490.1458");
//
//
//        }catch (BaseException exception){
//            return new BaseResponse<>(BaseResponseStatus.NEW_APPLE_USERS);
//        }
//
//
//
//
//
//        //회원 반환값 생성
//        String jwt=jwtService.createJwt(userInfo.getId(), userInfo.getUserName());
//        PostAppleLoginRes postAppleLoginRes=new PostAppleLoginRes(userInfo.getId(),jwt);
//
//
//
//        return new BaseResponse<>(BaseResponseStatus.SUCCESS_APPLE_LOGIN,postAppleLoginRes);
//    }

    /**
     * 자동 로그인 api
     */

    @GetMapping("/login/jwt")
    public BaseResponse<GetJwtRes> getJwt() {

        GetJwtRes getJwtRes=null;
        try {
            int userId = jwtService.getUserId();
            getJwtRes=new GetJwtRes(userInfoProvider.findByUser(userId).getChallengeStatus());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_JWT,getJwtRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 회원정보조회 api
     */

    @GetMapping("/users/{userIdx}")
    public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable Integer userIdx) {



        GetUserInfoRes getUserInfoRes = null;
        //1. userIdx null 값이거나 존재하지 않는 값일 때 예외처리
        if (userIdx == null || userIdx <= 0) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
        }
        //토큰 값 검증
        String jwt = null;


        //if(userIdx)

        //2 db에서 유저 정보 존재 유무 확인하기

        Integer id;

        try {
            id = jwtService.getUserId();

            if (id != userIdx) {
                return new BaseResponse<>(BaseResponseStatus.DO_NOT_MATCH_USERID);
            }
            getUserInfoRes = userInfoProvider.UserIdxRetrieveUserInfo(userIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_USER, getUserInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());

        }


    }


    /**
     * 회원가입 api
     * @RequestBody PostUserReq
     *
     */


    @ResponseBody
    @PostMapping("/users")
    public BaseResponse<PostUserRes> postUsers(@org.springframework.web.bind.annotation.RequestBody PostUserReq postUserReq)
    {
        //1 Body postUserReq validation

        //위도 ,경도 validation 나중에 추가하기
        if(postUserReq.getUserName()==null||postUserReq.getUserName().length()==0){
            return new BaseResponse<>(BaseResponseStatus.EMPTY_NICKNAME);
        }
        if(postUserReq.getLoginType()==null){
            return new BaseResponse<>(BaseResponseStatus.EMPTY_LOGIN_TYPE);
        }
        if(postUserReq.getParcelAddressing()==null||postUserReq.getParcelAddressing().length()==0){
            return new BaseResponse<>(BaseResponseStatus.EMPTY_ADDRESS);
        }
        if(postUserReq.getStreetAddressing()==null||postUserReq.getStreetAddressing().length()==0){
            return new BaseResponse<>(BaseResponseStatus.EMPTY_ADDRESS);
        }

        try{
            PostUserRes postUserRes=userInfoService.createUserInfo(postUserReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_USER,postUserRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }



    /**
     * 회원 정보 수정 API
     * @Param patchUserReq
     * @Return PatchUserRes
     *
     */
    @ResponseBody
    @PatchMapping("/users/{userIdx}")
    public BaseResponse<PatchUserRes> patchUsers(@PathVariable Integer userIdx,@org.springframework.web.bind.annotation.RequestBody PatchUserReq patchUserReq)
    {
        int id;
        if(userIdx==null||userIdx==0){
            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);

        }

        try {
            id=jwtService.getUserId();
            if(id!=userIdx){
                return new BaseResponse<>(BaseResponseStatus.DO_NOT_MATCH_USERIDX);
            }

            return new BaseResponse<>(BaseResponseStatus.SUCCESS_PATCH_USER,userInfoService.updateUserInfo(userIdx,patchUserReq));



        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }


    }


    /**
     * 기기토큰 등록 API
     * @Param postUserDeviceTokenReq
     * @Return BaseResponse<Void>
     *
     */

    @ResponseBody
    @PostMapping("/users/tokens")
    public BaseResponse<Void> postUserDeviceToken(@org.springframework.web.bind.annotation.RequestBody postUserDeviceTokenReq parameter)
    {
        System.out.println("postUserDeviceToken start");
        String userDeviceToken = parameter.getUserDeviceToken();

        if(userDeviceToken==null){
            return new BaseResponse<>(BaseResponseStatus.EMPTY_DEVICETOKEN);
        }

        //get UserIdx
        int userIdx;
        try {
            userIdx = jwtService.getUserId();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        try{
            userInfoService.createUserDeviceToken(userIdx,userDeviceToken);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }





}
////    /**
////     * 회원 조회 API
////     * [GET] /users/:userId
////     * @PathVariable userId
////     * @return BaseResponse<GetUserRes>
////     */
////    @ResponseBody
////    @GetMapping("/{userId}")
////    public BaseResponse<GetUserRes> getUser(@PathVariable Integer userId) {
////        if (userId == null || userId <= 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
////        }
////
////        try {
////            GetUserRes getUserRes = userInfoProvider.retrieveUserInfo(userId);
////            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_USER, getUserRes);
////        } catch (BaseException exception) {
////            return new BaseResponse<>(exception.getStatus());
////        }
////    }









//    @GetMapping("/jwt")
//    public BaseResponse<Void> jwt() {
//        try {
//            int userId = jwtService.getUserId();
//            userInfoProvider.retrieveUserInfo(userId);
//            return new BaseResponse<>(BaseResponseStatus.SUCCESS_JWT);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }


//    private final UserInfoProvider userInfoProvider;
//    private final  UserInfoService userInfoService;
//
//
//
//    @ResponseBody
//    @PostMapping("/login/kakao")
//    public BaseResponse<PostKakaoLoginRes> getKakaoUser(@RequestBody PostKakaoLoginReq postKakaoLoginReq ) throws BaseException {
//        //토큰값을 snsid로 바꿔서 provider에게 보내기
//        //postKakaoLoginReq.getAccessToken()
//
//
//        PostKakaoLoginRes postKakaoLoginRes=null;
//        //GetKakaoUserRes getKakaoUserRes;
//        RestTemplate rt = new RestTemplate();//http요청을 간단하게 해줄 수 있는 클래
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer "+ postKakaoLoginReq.getAccessToken());
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//
//        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
//                new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoProfileRequest,
//                String.class
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        KakaoProfile profile  =null;
//        //Model과 다르게 되있으면 그리고 getter setter가 없으면 오류가 날 것이다.
//        try {
//            profile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
//           // System.out.println("@@@@@@@@@@"+profile.getId());
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        String jwt=null;
//        System.out.println("########");
//        Userinfo userInfo =userInfoProvider.findOne(profile.getId());//snsId 값 넘김
//        if(userInfo!=null){//공백이 아닐경우 즉 해당 회원이 존재할 경우
//
//            postKakaoLoginRes=userInfoService.createUserJwt(userInfo.getId());
//            System.out.println("###777#######");
//
//        }
//
//
//        return new BaseResponse<>(BaseResponseStatus.SUCCESS_KAKAO_LOGIN,postKakaoLoginRes);
//    }
//
//
//}
////    private final UserInfoProvider userInfoProvider;
////    private final UserInfoService userInfoService;
////    private final JwtService jwtService;
////
////    @Autowired
////    public UserInfoController(UserInfoProvider userInfoProvider, UserInfoService userInfoService, JwtService jwtService) {
////        this.userInfoProvider = userInfoProvider;
////        this.userInfoService = userInfoService;
////        this.jwtService = jwtService;
////    }
////
////    /**
////     * 회원 전체 조회 API
////     * [GET] /users
////     * 회원 닉네임 검색 조회 API
////     * [GET] /users?word=
////     * @return BaseResponse<List<GetUsersRes>>
////     */
////    @ResponseBody
////    @GetMapping("") // (GET) 127.0.0.1:9000/users
////    public BaseResponse<List<GetUsersRes>> getUsers(@RequestParam(required = false) String word) {
////        try {
////            List<GetUsersRes> getUsersResList = userInfoProvider.retrieveUserInfoList(word);
////            if (word == null) {
////                return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_USERS, getUsersResList);
////            } else {
////                return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_SEARCH_USERS, getUsersResList);
////            }
////        } catch (BaseException exception) {
////            return new BaseResponse<>(exception.getStatus());
////        }
////    }
////
////    /**
////     * 회원 조회 API
////     * [GET] /users/:userId
////     * @PathVariable userId
////     * @return BaseResponse<GetUserRes>
////     */
////    @ResponseBody
////    @GetMapping("/{userId}")
////    public BaseResponse<GetUserRes> getUser(@PathVariable Integer userId) {
////        if (userId == null || userId <= 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
////        }
////
////        try {
////            GetUserRes getUserRes = userInfoProvider.retrieveUserInfo(userId);
////            return new BaseResponse<>(BaseResponseStatus.SUCCESS_READ_USER, getUserRes);
////        } catch (BaseException exception) {
////            return new BaseResponse<>(exception.getStatus());
////        }
////    }
////
////    /**
////     * 회원가입 API
////     * [POST] /users
////     * @RequestBody PostUserReq
////     * @return BaseResponse<PostUserRes>
////     */
////    @ResponseBody
////    @PostMapping("")
////    public BaseResponse<PostUserRes> postUsers(@RequestBody PostUserReq parameters) {
////        // 1. Body Parameter Validation
////        if (parameters.getEmail() == null || parameters.getEmail().length() == 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_EMAIL);
////        }
////        if (!ValidationRegex.isRegexEmail(parameters.getEmail())){
////            return new BaseResponse<>(BaseResponseStatus.INVALID_EMAIL);
////        }
////        if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_PASSWORD);
////        }
////        if (parameters.getConfirmPassword() == null || parameters.getConfirmPassword().length() == 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_CONFIRM_PASSWORD);
////        }
////        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
////            return new BaseResponse<>(BaseResponseStatus.DO_NOT_MATCH_PASSWORD);
////        }
////        if (parameters.getNickname() == null || parameters.getNickname().length() == 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_NICKNAME);
////        }
////
////        // 2. Post UserInfo
////        try {
////            PostUserRes postUserRes = userInfoService.createUserInfo(parameters);
////            return new BaseResponse<>(BaseResponseStatus.SUCCESS_POST_USER, postUserRes);
////        } catch (BaseException exception) {
////            return new BaseResponse<>(exception.getStatus());
////        }
////    }
////
////    /**
////     * 회원 정보 수정 API
////     * [PATCH] /users/:userId
////     * @PathVariable userId
////     * @RequestBody PatchUserReq
////     * @return BaseResponse<PatchUserRes>
////     */
////    @ResponseBody
////    @PatchMapping("/{userId}")
////    public BaseResponse<PatchUserRes> patchUsers(@PathVariable Integer userId, @RequestBody PatchUserReq parameters) {
////        if (userId == null || userId <= 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
////        }
////
////        if (!parameters.getPassword().equals(parameters.getConfirmPassword())) {
////            return new BaseResponse<>(BaseResponseStatus.DO_NOT_MATCH_PASSWORD);
////        }
////
////        try {
////            return new BaseResponse<>(BaseResponseStatus.SUCCESS_PATCH_USER, userInfoService.updateUserInfo(userId, parameters));
////        } catch (BaseException exception) {
////            return new BaseResponse<>(exception.getStatus());
////        }
////    }
////
////    /**
////     * 로그인 API
////     * [POST] /users/login
////     * @RequestBody PostLoginReq
////     * @return BaseResponse<PostLoginRes>
////     */
////    @PostMapping("/login")
////    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq parameters) {
////        // 1. Body Parameter Validation
////        if (parameters.getEmail() == null || parameters.getEmail().length() == 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_EMAIL);
////        } else if (!ValidationRegex.isRegexEmail(parameters.getEmail())) {
////            return new BaseResponse<>(BaseResponseStatus.INVALID_EMAIL);
////        } else if (parameters.getPassword() == null || parameters.getPassword().length() == 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_PASSWORD);
////        }
////
////        // 2. Login
////        try {
////            PostLoginRes postLoginRes = userInfoProvider.login(parameters);
////            return new BaseResponse<>(BaseResponseStatus.SUCCESS_LOGIN, postLoginRes);
////        } catch (BaseException exception) {
////            return new BaseResponse<>(exception.getStatus());
////        }
////    }
////
////    /**
////     * 회원 탈퇴 API
////     * [DELETE] /users/:userId
////     * @PathVariable userId
////     * @return BaseResponse<Void>
////     */
////    @DeleteMapping("/{userId}")
////    public BaseResponse<Void> deleteUsers(@PathVariable Integer userId) {
////        if (userId == null || userId <= 0) {
////            return new BaseResponse<>(BaseResponseStatus.EMPTY_USERID);
////        }
////
////        try {
////            userInfoService.deleteUserInfo(userId);
////            return new BaseResponse<>(BaseResponseStatus.SUCCESS_DELETE_USER);
////        } catch (BaseException exception) {
////            return new BaseResponse<>(exception.getStatus());
////        }
////    }
////
////    /**
////     * JWT 검증 API
////     * [GET] /users/jwt
////     * @return BaseResponse<Void>
////     */
////    @GetMapping("/jwt")
////    public BaseResponse<Void> jwt() {
////        try {
////            int userId = jwtService.getUserId();
////            userInfoProvider.retrieveUserInfo(userId);
////            return new BaseResponse<>(BaseResponseStatus.SUCCESS_JWT);
////        } catch (BaseException exception) {
////            return new BaseResponse<>(exception.getStatus());
////        }
////    }
////}
