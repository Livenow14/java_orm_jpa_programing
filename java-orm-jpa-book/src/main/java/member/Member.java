package member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 클래스를 분리해주면 오류가 나지 않는다
 */
@Getter
@Setter
@Entity
@Table(name="MEMBER")
public class Member {

    @Id
    @Column(name="ID")
    private String id;  //아아디

    @Column(name="NAME")
    private String username;

    //매핑 정보가 없는 필드 여기서는 데이터베이스가 대소문자를 구분하지 않는다고 가정
    private Integer age;

}
