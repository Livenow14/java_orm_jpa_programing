package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String city;
    private String street;
    private String zipcode;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    //== 연관관계 편의 메서드, 이는 한쪽에만 놔두는 것이 좋다(무한루프 때문에). 지금은 예제를 위해 양쪽에 적음 ==//
    public void setTeam(Team team){
        this.team = team;

        //무한루프에 빠지지 않도록 체크
        if(!team.getMembers().contains(this))
            team.getMembers().add(this);
    }



}
