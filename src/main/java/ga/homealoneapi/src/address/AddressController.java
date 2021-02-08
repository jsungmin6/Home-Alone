package ga.homealoneapi.src.address;

import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponse;
import ga.homealoneapi.config.BaseResponseStatus;
import ga.homealoneapi.src.address.model.GetRecentlyLocationRes;
import ga.homealoneapi.src.address.model.PostUserLocationReq;
import ga.homealoneapi.src.user.models.GetUserRes;
import ga.homealoneapi.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final JwtService jwtService;
    private final AddressService addressService;

    /**
     * 회원 최근 위치정보 리스트 조회 API
     * [GET] /users/recently-locations
     * @return BaseResponse<List<GetRecentlyLocationRes>>
     */
    @ResponseBody
    @GetMapping("/users/recently-locations")
    public BaseResponse<List<GetRecentlyLocationRes>> getRecentlyLocation() {
        System.out.println("getRecentlyLocation start");

        //jwt 토큰 에서 userIdx 얻기
        int userIdx;
        try {
            userIdx = jwtService.getUserId();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        System.out.println("userIdx :"+userIdx);

        try {
            List<GetRecentlyLocationRes> getRecentlyLocationRes1 = addressService.recentlyLocationByUserIdx(userIdx);//TODO: jwt에서 받은 userIdx 넣어줘야함.
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, getRecentlyLocationRes1);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }



    }
        /**
     * 회원 위치 정보조회 API
     * [POST] /users/locations
     * @RequestBody PostUserLocationReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PostMapping("/users/locations")
    public BaseResponse<Void> postUserLocation(@RequestBody PostUserLocationReq parameters) {
        System.out.println("postUserLocation start");
        // 1. Body Parameter Validation
        if (parameters.getLatitude() == 0) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_LATITUDE);
        }
        if (parameters.getLongitude() == 0) {
            return new BaseResponse<>(BaseResponseStatus.EMPTY_LONGITUDE);
        }

        //jwt 토큰 에서 userIdx 얻기
        int userIdx;
        try {
            userIdx = jwtService.getUserId();
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        System.out.println("userIdx :"+userIdx);

        // 2. Post UserInfo
        try {
            boolean isInHome = addressService.isInHome(parameters,userIdx);
            if(isInHome){
                return new BaseResponse<>(BaseResponseStatus.SUCCESS_IN_HOME);
            }
            return new BaseResponse<>(BaseResponseStatus.FAILED_TO_NOT_IN_HOME);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
