package domain.joinTableExam;

import javax.persistence.*;

@Entity
public class Child {



    @Id
    @GeneratedValue
    @Column(name="child_id")
    private Long id;

    private String name;

    /**
     * 일대일 조인 테이블 양방향 매핑
     */
    @OneToOne(mappedBy = "child")
    private Parent parent;


    /**
     * 다대일 조인 테이블
     */
    @ManyToOne(optional = "false")
    @JoinTable(name = "parent_child" ,
            joinColumns = @JoinColumn(name ="child_id"),
            inverseJoinColumns = @JoinColumn(name="parent_id"))
    private Parent parent;
}
