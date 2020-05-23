package domain.item;
import domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)     // 조인전략: 상속 매핑은 부모 클래스에 이 annotation을 사용해야한다. 그리고 매핑 전략을 지정해야 하는데, 여기서는 조인 전략을 사용하므로 JOINED를 사용했다.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)     // 단일 테이블전략: 이름 그대로 테이블을 하나만 사용한다. 주의점은 자식 엔티티가 매핑한 컬럼은 모두 null을 허용해야한다. 왜냐하면 하나의 엔티티만 저장되면 다른 엔티티는 null이 되므로
@DiscriminatorColumn(name="dtype")                  // 부모 클래스에서 구분 컬럼을 지정한다. 이 컬럼으로 저장된 자식 테이블을 구분 할 수 있다. 기본 값이 DTYPE이므로 @DiscriminatorColumn으로 줄여 사용해도됨.
public abstract class Item {

    @Id @GeneratedValue
    @Column(name ="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")                         //**
    private List<Category> categories; //**


}
