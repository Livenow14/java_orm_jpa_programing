package com.livenow.ch11jpashop.domain.test;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<MemberTest> members = new ArrayList<>();

    @Builder
    private Team(String name) { //여기 그냥 members도 param으로 넣었다가 에러 팡!
        this.name = name;
    }
}