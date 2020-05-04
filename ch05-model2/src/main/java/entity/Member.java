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


}
