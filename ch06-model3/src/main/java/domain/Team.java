package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {
    @Id
    @Column(name="TEAM_ID")
    private String id;

    private String name;

    @OneToMany(mappedBy = "team")               // 연관관계의 주인이 아닐때 mapped를 쓴다. 객체는 양방향 연관관계라는 것이 없다. 그래서 양방향 처럼 보이지만 실제로는 2개의 단방향이 있기때문에 연관관계의 주인을 정하는 것이다.
    private List<Member> members = new ArrayList<>();   // 여기서 연관관계의 주인은 Member.team이 된다.
                                                // mappedBy 속성의 값으로는 연관관계의 주인인 team을 주면된다. 여기서는 Member 엔티티의 team필드를 말한다.
                                                // 연관관계의 주인만 데이터베이스 연관관계와 매핑되고 외래 키를 관리할 수 있다. 주인이 아닌 반대편은 읽기만 가능하고 외래키를 변경하지는 못한다.
    //== 연관관계 편의 메서드, 이는 한쪽에만 놔두는 것이 좋다(무한루프 때문에). 지금은 예제를 위해 양쪽에 적음 ==//
    public void addMember(Member member){
        this.members.add(member);
        if(member.getTeam() != this)            //무한루프에 빠지지 않도록 체크
            member.setTeam(this);
    }
                                                //일대다는 양방향 매핑이 없다( 1 에서 mappedBy 옵션이 없는것을 생각하면 된다. )


}
