package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    /**
     *  9.2.2 임베디드 타입과 연관관계
     */
    @Embedded Address homeAddress; //집주소
    @Embedded PhoneNumber phoneNumber;    //근무기간

    /**
     *  9.2.3 속성 재정의 가능
     *  이는 어노테이션을 너무 많이 사용해서 코드가 지저분해진다.
     *  다행히 한 엔티티에 같은 임베디드 타입을 중복해서 사용하는 일은 많지 않다.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="city", column = @Column(name="company_city")),
            @AttributeOverride(name="street", column = @Column(name="company_street")),
            @AttributeOverride(name="zipcode", column = @Column(name="company_zipcode"))
    })
    Address companyAddress;

    /**
     *  9.2.4 임베디드 타입과 null
     *  임베디드 타입이 null이면 매핑한 컬럼은 모두 null이된다.
     */


    /**
     *  9.3.1 값 타입 공유 참조
     *  임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 안된다.
     *  대신 값을 복사해서 사용해야한다. (Address가 정의 되었을때 새로운 Address(ex. Address newAddress= address.clone()) 정의)
     *
     *  기본 타입은 값을 대입하면 값을 복사해서 전달하지만
     *  객체 타입은 객체에 값을 대입하면 항상 참조값을 전달한다. 그렇기 때문에 같은 인스턴스를 공유 참조한다.
     *
     *  자바는 단지 자바 기본 타입이면 값을 복사해서 넘기고, 객체면 참조를 넘길 뿐이기 때문에
     *  객체의 공유 참조는 피할 수 없다. 따라서 Address 객체의 setCity() 같은 수정자 메소드를 제거해서
     *  부작용의 발생을 막는 방법이있다.
     *
     *  이는 생성자로만 값을 설정하고, Setter를 쓰지 않는 것이다. (불변 객체를 만드는 것 )
     *  정리하면 불변이라는 작은 제약으로 부작용이라는 큰 재앙을 막을 수 있다.
     */

    /**
     * 9.4 값 타입의 비교
     *
     * 자바가 제공하는 객체비교는 2가지이다.
     * 동일성(identity) 비교 : 인스턴스의 참조 값을 비교, ==사용
     * 동등성(Equivalence) 비교 : 인스턴스의 값을 비교, equals() 사용
     *
     * Address 값 타입을 a == b로 동일성 비교하면 둘은 서로 다른 인스턴스이므로 결과는 거짓이다. 하지만 이것은 기대하는 결과가 아니다.
     * 값 타입은 비록 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 봐야한다. 따라서 값 타입을 비교할 때는
     * a.equals(b)를 사용해서 동등성 비교를 해야한다.
     * 물론 Address의 equals() 메소드를 재정의 해야한다. 값 타입의 equals()메소드를 재정의 할 때는 보통 모든 필드의 값을 비교하도록 구현한다.
     * equals()를 재정의하면 hashCode()도 재정의하는 것이 안전하다.
     */




}
