package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //== 연관관계 메서드 ==//
    public void setMember(Member member){
        //기존 관계 제거
        if(this.member !=null)
            this.member.getOrders().remove(this);
        this.member=member;
        member.getOrders().add(this);

    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
