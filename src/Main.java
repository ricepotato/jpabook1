
import javax.persistence.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello jpa!");

        // EntityManagerFactory 애플리케이션에서 한개의 객체만 생성해야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        // EntityManager CRUD 할 수 있는 객체 thread 간 공유 불가
        // 비용이 거의 들지 않는다.
        EntityManager em = emf.createEntityManager();
        // 트랜젝
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            logic(em);
            // 영속성 컨텍스트에 있는 sql 을 모두 db 로 보낸다.
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
        // 객체를 만들었을때는 비영속 상태이다.
        Member member = new Member();

        // 여기서는 query 가 db 로 전달되지 않는다.
        member.setId(id);
        member.setUsername("사공석준");
        member.setAge(2);

        // 영속성 컨텍스트에 저장한다.
        em.persist(member);

        // 1차 캐시에 있는 값을 변경한다.
        member.setAge(20);

        // 캐시에 있는 내용이 조회된다. db 에는 아직 입력되지 않은 상태이다. 성능상 이점을 가진다.
        // 캐시에 데이터가 없으면 db 에 있는 데이터가 조회된다. db 에서 조회한 데이터를
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.toString());

        TypedQuery<Member> query = em.createQuery("select m from m", Member.class);
        List<Member> members = query.getResultList();
        System.out.println("members.size=" + members.size());

        em.remove(member);


    }
}
