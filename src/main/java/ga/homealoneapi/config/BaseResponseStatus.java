package ga.homealoneapi.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    // 1000 : 요청 성공
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    SUCCESS_READ_USERS(true, 1010, "회원 전체 정보 조회에 성공하였습니다."),
    SUCCESS_READ_USER(true, 1011, "회원 정보 조회에 성공하였습니다."),
    SUCCESS_POST_USER(true, 1012, "회원가입에 성공하였습니다."),
    SUCCESS_LOGIN(true, 1013, "로그인에 성공하였습니다."),
    SUCCESS_JWT(true, 1014, "JWT 검증에 성공하였습니다."),
    SUCCESS_DELETE_USER(true, 1015, "회원 탈퇴에 성공하였습니다."),
    SUCCESS_PATCH_USER(true, 1016, "회원정보 수정에 성공하였습니다."),
    SUCCESS_READ_SEARCH_USERS(true, 1017, "회원 검색 조회에 성공하였습니다."),
    SUCCESS_KAKAO_LOGIN(true, 1018, "카카오 로그인 성공."),
    SUCCESS_IN_HOME(true, 1019, "집 인증 성공"),
    NEW_KAKAO_USERS(true, 1200, "카카오 신규 회원입니다."),
    SUCCESS_APPLE_LOGIN(true, 1210, "애플 로그인 성공."),
    NEW_APPLE_USERS(true, 1211, "애플 신규 회원입니다."),



    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_USERID(false, 2001, "유저 아이디 값을 확인해주세요."),
    EMPTY_JWT(false, 2010, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2011, "유효하지 않은 JWT입니다."),
    EMPTY_EMAIL(false, 2020, "이메일을 입력해주세요."),
    INVALID_EMAIL(false, 2021, "이메일 형식을 확인해주세요."),
    EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요."),
    EMPTY_CONFIRM_PASSWORD(false, 2031, "비밀번호 확인을 입력해주세요."),
    WRONG_PASSWORD(false, 2032, "비밀번호를 다시 입력해주세요."),
    DO_NOT_MATCH_PASSWORD(false, 2033, "비밀번호와 비밀번호확인 값이 일치하지 않습니다."),
    EMPTY_NICKNAME(false, 2040, "닉네임을 입력해주세요."),
    DO_NOT_MATCH_USERID(false, 2041, "회원 인덱스 값이 일치하지 않습니다."),
    EMPTY_LOGIN_TYPE(false, 2042, "sns 로그인 유형을 입력해주세요."),
    EMPTY_ADDRESS(false, 2043, "주소를 입력해주세요."),
    EMPTY_LATITUDE(false, 2044, "위도를 입력해주세요."),
    EMPTY_LONGITUDE(false, 2045, "경도를 입력해주세요."),
    EMPTY_STARTTIME(false, 2046, "시작시간을 입력해 주세요"),
    EMPTY_ENDTIME(false, 2047, "종료시간을 입력해 주세요"),
    EMPTY_DEVICETOKEN(false, 2300, "userDeviceToken을 입력해 주세요"),
    DO_NOT_MATCH_USERIDX(false, 2200, "JWT토큰의 userId와 PathVariable 의 userId가 일치하지 않습니다."),
    INVALID_APPLE(false, 2210, "유효하지 않은 애플 로그인 값 입니다"),
    

    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    NOT_FOUND_USER(false, 3010, "존재하지 않는 회원입니다."),
    DUPLICATED_USER(false, 3011, "이미 존재하는 회원입니다."),
    FAILED_TO_GET_USER(false, 3012, "회원 정보 조회에 실패하였습니다."),
    FAILED_TO_POST_USER(false, 3013, "회원가입에 실패하였습니다."),
    FAILED_TO_LOGIN(false, 3014, "로그인에 실패하였습니다."),
    FAILED_TO_DELETE_USER(false, 3015, "회원 탈퇴에 실패하였습니다."),
    FAILED_TO_PATCH_USER(false, 3016, "개인정보 수정에 실패하였습니다."),
    FAILED_TO_CHALLENGE_TIME(false, 3017, "챌린지 중이 아닙니다."),
    FAILED_TO_ALREADY_START(false, 3018, "이미 챌린지 중 입니다."),
    FAILED_TO_NOT_IN_HOME(false, 3025, "현재 집에 있지 않습니다."),
    NOT_FOUND_CHALLENGE(false, 3026, "존재하지 않는 챌린지 입니다."),
    FAILED_TO_ALREADY_SUCCESS(false, 3027, "이미 성공한 챌린지 입니다."),
    FAILED_TO_MATCH_CHALLENGE(false, 3028, "진행중인 챌린지가 아닙니다."),
    INVALID_KAKAO(false, 3200, "유효하지 않은 access-token입니다."),
    NOT_FOUND_USERDEVICETOKEN(false, 3300, "유효한 userDeviceToken이 없습니다."),


    // 4000 : Database 오류
    SERVER_ERROR(false, 4000, "서버와의 통신에 실패하였습니다."),
    DATABASE_ERROR(false, 4001, "데이터베이스 연결에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
