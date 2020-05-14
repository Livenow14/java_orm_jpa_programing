package domain.onetoone;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Locker {

    /**
     * 1대1 단방향 예제
     */
    @Id @GeneratedValue
    @Column(name="locker_id")
    private Long id;
    
    private String name;

    /**
     * 1대1 양방향 예제
     */
    @OneToOne(mappedBy = "locker")
    private OTOMember otoMember;
    
}
