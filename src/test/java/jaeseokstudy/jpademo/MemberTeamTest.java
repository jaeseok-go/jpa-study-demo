package jaeseokstudy.jpademo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberTeamTest {

    @Autowired
    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void init() {
        em = emf.createEntityManager();
    }

    @AfterEach
    void destroy() {
        em.close();
    }

    @Test
    @DisplayName("단방향 연관관계를 이용해서 정상적으로 데이터를 저장하고 조회한다.")
    void saveTest() {
        // given
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member("member2", "회원2");
        member2.setTeam(team1);
        em.persist(member2);

        // when
        Member findMember = em.find(Member.class, "member1");

        // then
        Assertions.assertEquals("member1", findMember.getId());
        Assertions.assertEquals("team1", findMember.getTeam().getId());
    }
}
