package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {
    @Id
    @Column(name="TEAM_ID")
    private String id;

    private String name;

    public Team(String id, String name) {
        this.id=id;
        this.name=name;
    }
}
