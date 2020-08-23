import lombok.Data;

import javax.persistence.*;

/**
 * @Converter
 * 엔티티의 데이터를 변환해서 데이터베이스에 저장
 * ex, vip 여부를 자바의 boolean 타입을 사용하고 싶다면, 0 or 1로 저장되는데 이를
 * Y, N으로 저장하고 싶을 때 사용
 *
 * @Convert 속성
 * convert = 사용할 컨버터를 지정
 * attributeName 컨버터를 적용할 필드를 지정
 * disableConversion 글로벌 컨버터나 상속 받은 컨버터를 사용하지 않는다.
 */
@Entity
@Data
/**
 * 클래스 레벨에서도 설정할 수 있다. 단, 이때는 attributeName 속성을 사용해서 어떤 필드에 컨버터를 적용할지 명시해야함
 */
//@Convert(converter = BooleanToYNConverter.class, attributeName = "vip")
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private String id;
    private String username;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean vip;
}
