package ga.homealoneapi.src.user;

import ga.homealoneapi.config.DeleteStatus;
import ga.homealoneapi.src.address.model.Address;
import ga.homealoneapi.src.fcm.UserDeviceTokenDto;
import ga.homealoneapi.src.user.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserInfoRepository {

    private final EntityManager em;



    //성공시 findBySnsUser로 변

    //카카오 유저확인
    public List<UserInfo> findByKakaoUser(String snsId){
        List<UserInfo> result=em.createQuery(" select u from UserInfo u where u.snsId= :snsId and u.loginType =:loginType ", UserInfo.class)
                .setParameter("snsId",snsId)
                .setParameter("loginType",LoginType.KAKAO)
                .getResultList();
        return result;

    }

    //
    public List<UserInfo> findByAppleUser(String snsId){
        List<UserInfo> result=em.createQuery(" select u from UserInfo u where u.snsId= :snsId and u.loginType =:loginType ", UserInfo.class)
                .setParameter("snsId",snsId)
                .setParameter("loginType",LoginType.APPLE)
                .getResultList();
        return result;

    }

    public List<UserInfo> findByUser(String snsId){
        List<UserInfo> result=em.createQuery(" select u from UserInfo u where u.snsId= :snsId ", UserInfo.class)
                .setParameter("snsId",snsId)
                .getResultList();
        return result;

    }




    public  UserInfo findByUserIdx(Integer userIdx){
        UserInfo result=em.createQuery(" select u from  UserInfo  u where  u.id= :userIdx",UserInfo.class)
                        .setParameter("userIdx",userIdx)
                        .getSingleResult();
        return result;

    }





    public void save(UserInfo userInfo){
           em.persist(userInfo);
           System.out.println("저장 쿼리 실행");


    }

    // 변경 전Integer snsId
    public List<UserInfo> findBySnsId(String snsId){

        //db에서 userInfo조회
        List<UserInfo> result=em.createQuery("select u from UserInfo u where u.snsId=:snsId",UserInfo.class)
                .setParameter("snsId",snsId)
                .getResultList();

        return result;

    }

    public UserInfo updateByUserIdx(Integer userIdx, PatchUserReq patchUserReq){

        //db 에서 해당 회원 정보를 가져옴
        //영속성 컨텍스트에 올려놓음
        //변경감지 가능하도록

        List<Address> ListAddress;
        UserInfo userInfo=em.createQuery("select u from UserInfo  u where u.id= :userIdx",UserInfo.class)
                        .setParameter("userIdx",userIdx)
                        .getSingleResult();
        if(patchUserReq.getPushStatus()!=null){
            userInfo.setPushStatus(patchUserReq.getPushStatus());
            em.persist(userInfo);

        }
        if(patchUserReq.getStreetAddressing()!=null){ //도로명 주소 null이 아닐경우로 일단 임시로 해놓음 제약조건 추가하기
            userInfo.setLatitude(patchUserReq.getLatitude());
            userInfo.setParcelAddressing(patchUserReq.getParcelAddressing());
            userInfo.setLongitude(patchUserReq.getLongitude());
            userInfo.setStreetAddressing(patchUserReq.getStreetAddressing());
            em.persist(userInfo);

            //주소 저장

            ListAddress=em.createQuery("select a from Address a where a.parcelAddressing=:parcelAddressing and a.streetAddressing=:streetAddressing and a.userInfo.id=:userIdx",Address.class)
                    .setParameter("parcelAddressing",patchUserReq.getParcelAddressing())
                    .setParameter("streetAddressing",patchUserReq.getStreetAddressing())
                    .setParameter("userIdx",userIdx)
                    .getResultList();

            //중복 주소 없을경우 주소 테이블에 추가

            if(ListAddress.isEmpty())
            {
                System.out.println(ListAddress);
                DeleteStatus deleteStatus=DeleteStatus.N;
                Address address=new Address(patchUserReq.getLatitude(), patchUserReq.getLongitude(), patchUserReq.getParcelAddressing(), patchUserReq.getStreetAddressing(),deleteStatus,userInfo);
                em.persist(address);

            }








        }

       return userInfo;
    }

    public List<UserDeviceTokenDto> findAllUserDeviceToken(){

        List<UserDeviceTokenDto> result=em.createQuery("select distinct new ga.homealoneapi.src.fcm.UserDeviceTokenDto(u.userDeviceToken) from UserInfo u where u.isDeleted='N' and u.pushStatus='Y' and u.userDeviceToken is not null",UserDeviceTokenDto.class)
                .getResultList();

        for (UserDeviceTokenDto userDeviceTokenDto : result) {
            System.out.println("Token : "+userDeviceTokenDto.getUserDeviceToken());
        }

        return result;

    }


    public List<UserInfo> findByUserJwt(Integer userIdx) {
        List<UserInfo> result=em.createQuery("select  u from UserInfo u where u.id=:userIdx")
                .setParameter("userIdx",userIdx)
                .getResultList();
        return result;
    }
}
