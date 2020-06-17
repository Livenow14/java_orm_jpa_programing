package com.livenow.ch11jpashop.domain.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberTestTest {
    @Autowired
    MemberTestRepository memberTestRepository;
    @Autowired TeamRepository teamRepository;

    @DisplayName("연관관계 테스트")
    @Test
    public void test(){
        //given
        Team team = Team.builder()
                .name("TeamA")
                .build();

        MemberTest member = MemberTest.builder()
                .username("member1")
                .team(team)
                .build();

        //when
        team.getMembers().add(member);
        memberTestRepository.save(member);

        //then
        MemberTest member1 = memberTestRepository.findAll().get(0);
        String username= member1.getUsername();
        assertThat(username).isEqualTo(member.getUsername());

        List<MemberTest> members = team.getMembers();
        for (MemberTest memberTest : members) {
            System.out.println("memberTest = " + memberTest.getUsername());
        }

    }


}