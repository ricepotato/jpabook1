
import javax.persistence.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello jpa!");

        // EntityManagerFactory 애플리케이션에서 한개의 객체만 생성해야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        // EntityManager CRUD 할 수 있는 객체 thread 간 공유 불가
        EntityManager em = emf.createEntityManager();
        // 트랜젝
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            logic(em);
            tx.commit();
        }catch (Exception e ){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(EntityManager em){
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("사공석준");
        member.setAge(2);

        em.persist(member);

        member.setAge(20);

        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.toString());

        TypedQuery<Member> query = em.createQuery("select m from m", Member.class);
        List<Member> members = query.getResultList();
        System.out.println("members.size=" + members.size());

        em.remove(member);


    }
}
