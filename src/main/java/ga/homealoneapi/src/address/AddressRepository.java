package ga.homealoneapi.src.address;

import ga.homealoneapi.src.address.model.Address;
import ga.homealoneapi.src.address.model.GetRecentlyLocationRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AddressRepository {

    private final EntityManager em;

    public List<GetRecentlyLocationRes> findAll(int userIdx) {
        List<GetRecentlyLocationRes> result = em.createQuery("select distinct new ga.homealoneapi.src.address.model.GetRecentlyLocationRes(a.parcelAddressing, a.streetAddressing, a.latitude, a.longitude) from Address a where a.userInfo.id = :userIdx", GetRecentlyLocationRes.class)
                .setParameter("userIdx", userIdx)
                .getResultList();
        for (GetRecentlyLocationRes address : result) {
            System.out.println("address :"+ address.getLatitude());
        }

        return result;
    }

    //address 저장
    public void save(Address address){
        em.persist(address);

    }

}