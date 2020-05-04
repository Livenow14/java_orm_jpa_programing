package entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by holyeye on 2014. 3. 11..
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String name;

    private String city;
    private String street;
    private String zipcode;

    @ManyToOne
    @JoinColumn(name="TEAM_ID")
    private Team team;

    public void setTeam(Team team){
        if(this.team!=null){                            //기존 관계를 삭제해줘야함 안그러면 영속성 컨텍스트에 남아있게됨
            this.team.getMembers().remove(this);
        }
        this.team=team;
        team.getMembers().add(this);        //set 메소드 하나로 양방향 관계를 모두 설정
    }


}
