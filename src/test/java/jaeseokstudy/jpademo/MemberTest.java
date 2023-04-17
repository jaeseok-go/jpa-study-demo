package jaeseokstudy.jpademo;

import jakarta.persistence.*;
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
}