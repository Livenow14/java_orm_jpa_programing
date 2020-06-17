package com.livenow.ch11jpashop.domain.test;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MemberTest {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_TEST_ID")
    private Long id;

    private String username;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "TEAM_ID")
    private Team team;

    @Builder
    private MemberTest(String username, Team team) {
        this.username = username;
        this.team = team;
    }
}