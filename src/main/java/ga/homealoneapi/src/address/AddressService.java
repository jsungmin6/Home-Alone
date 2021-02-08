package ga.homealoneapi.src.address;

import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponseStatus;
import ga.homealoneapi.src.address.model.Address;
import ga.homealoneapi.src.address.model.GetRecentlyLocationRes;
import ga.homealoneapi.src.address.model.PostUserLocationReq;
import ga.homealoneapi.src.user.UserInfoRepository;
import ga.homealoneapi.src.user.models.UserInfo;
import ga.homealoneapi.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final JwtService jwtService;
    private final UserInfoRepository userInfoRepository;

    /**
     * 회원 최근 위치정보 리스트 조회
     * @param userIdx
     * @return List<GetRecentlyLocationRes>
     * @throws BaseException
     */
    public List<GetRecentlyLocationRes> recentlyLocationByUserIdx(int userIdx) throws BaseException{
        List<GetRecentlyLocationRes> address;
//        List<GetRecentlyLocationRes> result;

        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findByUserIdx(userIdx);
            System.out.println("challengeStatus: "+userInfo.getChallengeStatus());
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }

        try{
            address = addressRepository.findAll(userIdx);
//            result = address.stream()
//                    .map(o -> new GetRecentlyLocationRes(o))
//                    .collect(toList());
            return address;
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);
        }

    }

    /**
     * 회원 위치 정보조회 API
     * @param parameters, userIdx
     * @return boolean
     * @throws BaseException
     */
    public boolean isInHome(PostUserLocationReq parameters, int userIdx) throws BaseException {

        UserInfo userInfo;

        try{
            userInfo = userInfoRepository.findByUserIdx(userIdx);
            System.out.println("challengeStatus: "+userInfo.getChallengeStatus());
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        // 킬로미터(Kilo Meter) 단위
        double latitude1 = parameters.getLatitude();
        double longitude1 = parameters.getLongitude();
        double latitude2 = userInfo.getLatitude();
        double longitude2 = userInfo.getLongitude();

        System.out.println("latitude1 : "+latitude1);
        System.out.println("longitude1 : "+longitude1);
        System.out.println("latitude2 : "+latitude2);
        System.out.println("longitude2 : "+longitude2);

        double distanceKiloMeter =
                distance(latitude1, longitude1, latitude2, longitude2, "kilometer");

        System.out.println("distanceKiloMeter : "+distanceKiloMeter);

        if ((int)distanceKiloMeter <=  5) {
            return true;
        }
        return false;
    }

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     * @return
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
