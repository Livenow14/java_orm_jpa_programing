package domain.proxyExam;

import javax.persistence.EntityManager;

public class Logic {

    /**
     *회원과 팀 정보를 출력하는 비즈니스 로직
     */
    public static void printUserAndTeam(String memberId){
        Member member = em.find(Member.class, memberId);
        Team team= member.getTeam();
        System.out.println("member = " + member.getUsername());
        System.out.println("team.getName() = " + team.getName());
    }

    /**
     * 회원 정보만 출력하는 비즈니스 로직
     * 이때 지연로딩 사용한다.
     *
     * */

    public String printUser(String memberId){
        Member member = em.find(Member.class,memberId);
        System.out.println("member.getUsername() = " + member.getUsername());
    }
}
