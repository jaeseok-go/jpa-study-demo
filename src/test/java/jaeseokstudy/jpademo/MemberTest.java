package jaeseokstudy.jpademo;

import jakarta.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberTest {
    @PersistenceUnit
    EntityManagerFactory emf;
    @Test
    void example() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            logic(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
    }

    private void logic(EntityManager em) {
        String id = "id1";

        Member member = new Member();
        member.setId(id);
        member.setUsername("재석");
        member.setAge(2);

        // 등록
        em.persist(member);

        // 수정
        member.setAge(20);

        // 한 건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age=" + findMember.getAge());

        // 목록 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        System.out.println("members.size=" + members.size());

        // 삭제
        em.remove(member);
    }

    @DisplayName("영속성 컨텍스트에서 같은 id로 엔티티를 조회했을 때, 항상 같은 인스턴스를 반환한다.")
    @Test
    void equalsTest() {
        EntityManager em = emf.createEntityManager();

        String id = "member1";

        Member member = new Member();
        member.setId(id);
        member.setUsername("재석");
        member.setAge(2);

        // 등록
        em.persist(member);

        Member member1 = em.find(Member.class, id);
        Member member2 = em.find(Member.class, id);

        em.close();

        Assertions.assertTrue(member1 == member2);
    }

    @Test
    void lazyWriteTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        Member member1 = new Member();
        member1.setId("member1");
        member1.setUsername("회원1");
        member1.setAge(1);
        System.out.println("=== create member1 instance ===");

        Member member2 = new Member();
        member2.setId("member2");
        member2.setUsername("회원2");
        member2.setAge(2);
        System.out.println("=== create member2 instance ===");

        tx.begin();

        em.persist(member1);
        System.out.println("=== persist member1 ===");

        em.persist(member2);
        System.out.println("=== persist member2 ===");

        System.out.println("=== commit start ===");
        tx.commit();
        System.out.println("=== commit end ===");

        em.close();
    }
}