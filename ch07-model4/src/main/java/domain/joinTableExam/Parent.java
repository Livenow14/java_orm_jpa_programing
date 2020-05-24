package domain.joinTableExam;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id @GeneratedValue
    @Column(name="parent_id")
    private Long id;

    private String name;


    /**
     * 일대일 조인 테이블 매핑
     */
    @OneToOne
    @JoinTable(name = "parent_child" ,
            joinColumns = @JoinColumn(name ="parent_id"),
            inverseJoinColumns = @JoinColumn(name="child_id"))
    private Child child;

    /**
     * 일대다 조인 테이블 매핑
     */
    @OneToMany(mappedBy = "parent")
    private List<Child> childs = new ArrayList<>();

}
