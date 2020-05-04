package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Item {

    @Id @GeneratedValue
    @Column(name= "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

}
