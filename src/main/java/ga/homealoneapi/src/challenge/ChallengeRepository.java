package ga.homealoneapi.src.challenge;

import ga.homealoneapi.src.challenge.model.Challenge;
import ga.homealoneapi.src.user.models.ChallengeStatus;
import ga.homealoneapi.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ChallengeRepository {

    private final EntityManager em;

    public Challenge findRecentlyById(int userIdx) {
        Challenge result = em.createQuery("select c from Challenge c where c.userInfo.id = :userIdx ORDER BY c.createdAt DESC", Challenge.class)
                .setParameter("userIdx", userIdx)
                .setFirstResult(0)
                .setMaxResults(1)
                .getSingleResult();
        System.out.println("challenge :"+result);
        return result;
    }


    public UserInfo findChallengeStatusById(int userIdx) {
        System.out.println("findChallengeStatusById start");
        UserInfo result = em.createQuery("select u from UserInfo u where u.id = :userIdx", UserInfo.class)
                .setParameter("userIdx", userIdx)
                .getSingleResult();

        System.out.println("ChallengeStatus: "+result);
        System.out.println("==");
        return result;
    }

    public int save(Challenge challenge) {
        em.persist(challenge);
        return challenge.getId();
    }

    public Challenge findById(int challengeIdx) {
        System.out.println("Challenge findById start");
        Challenge result = em.createQuery("select c from Challenge c where c.id = :challengeIdx",Challenge.class)
                .setParameter("challengeIdx", challengeIdx)
                .getSingleResult();
        return result;

    }
}
