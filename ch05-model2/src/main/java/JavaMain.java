import entity.Member;
import entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JavaMain {
    public static void main(String[] args) {

        //엔티티 메니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작

            //5.2.1 회원 저장 로직
            testSave(em);

            //5.2.2 회원 조회 로직
            queryLogicJoin(em);

            //5.2.3 회원 수정 로직, em.update() 같은 메서드가 없고, 영속성 컨텍스트에 의해 불러운 엔티티의 값만 변경해두면 트렌잭션을 커밋할 때 플러시가 일어나면서 변경 감지 기능이 작동하고,
            // 그리고 변경사항을 데이터베이스에 자동으로 반영한다.
            updateRelation(em);

            //5.2.4 연관관계 제거
            deleteRelation(em);

            //5.2.5 연관된 엔티티 삭제, 연관된 엔티티를 삭제하려면 기존에 있던 연관관계를 먼저 제거하고 삭제해야 한다. 안그러면 외래키 제약조건으로 인해, 오류가 발생한다.

            //5.6 양방향 연관관계의 주의점, 가장 흔히 하는 실수는 연관관계의 주인에는 값을 입력하지 않고, 주인이 아닌 곳에만 값을 입력하는 것이다.

            // 5.6.2 연관관계 편의 메서드
            testORM_양방향_리팩토링(em);

            tx.commit();//트랜잭션 커밋

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }

    private static void testORM_양방향_리팩토링(EntityManager em) {
        Team team1 = new Team();
        team1.setId("team2");
        team1.setName("팀2");
        em.persist(team1);

        //회원1 저장
        Member member1 = new Member();
        member1.setId("member1");
        member1.setName("회원1");
        member1.setTeam(team1);         //양방향 설정
        em.persist(member1);

    }

    private static void deleteRelation(EntityManager em) {
        Member member1 = em.find(Member.class, "member1");
        member1.setTeam(null);  //연관관계제거

    }

    private static void updateRelation(EntityManager em) {
        //새로운 팀2
        Team team2 = new Team();
        team2.setId("team2");
        team2.setName("팀2");
        em.persist(team2);

        //회원1에 새로운 팀2 설정
        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);


    }

    //5.2.1 저장
    private static void testSave(EntityManager em) {
        //팀1 저장
        Team team1 = new Team();
        team1.setId("team1");
        team1.setName("팀1");
        em.persist(team1);

        //회원1 저장
        Member member1 = new Member();
        member1.setId("member1");
        member1.setName("회원1");
        member1.setTeam(team1);
        em.persist(member1);

        //회원2 저장
        Member member2 = new Member();
        member2.setId("member2");
        member2.setName("회원2");
        member2.setTeam(team1);
        em.persist(member2);
    }
    // 5.2.2 조회 로직
    private static void queryLogicJoin(EntityManager em) {
        String jpql ="select m from Member m join m.team t where " + "t.name=:teamName";
        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName","팀1")
                .getResultList();

        for(Member member : resultList){
            System.out.println("[query] member.username=" + member.getName());
        }

    }



    public static void logic(EntityManager em) {

    }
}
