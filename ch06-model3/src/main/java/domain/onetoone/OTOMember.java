package domain.onetoone;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter

/**
 * 1대1 단방향 예제
 */
public class OTOMember {

    @Id @GeneratedValue
    @Column(name ="member_id")
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name="locker_id")
    private Locker locker;


}


