package member;

import lombok.Getter;
import lombok.Setter;
import role.RoleType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    public String id;

    @Column(name="name", nullable = false, length =10)// 필수로 입력되야하고, 10자를 초과하면 안됨
    public String username;

    private Integer age;

    //== 추가 ==
    //enumType.STRING이어야지 오류가 발생하지 않음.
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

}
