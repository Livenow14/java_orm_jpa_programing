package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Getter
@Setter
/**
 * 임베디드 타입은 기본 생성자가 필수이다.
 * 엔티티의 값일 뿐이어서 값이 속한 엔티티의 테이블에 매핑한다.
 * 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많다.
 * ORM을 사용하지 않고 개발하면 테이블 컬럼과 객체 필드를 대부분 1:1 매핑한다.
 *
 * 임베디드 타입은 값 타입을 포함하거나 엔티티를 참조할 수 있다.
 */
public class Address {

    protected Address() {
    }

    private String city;
    private String street;
    private String state;
    @Embedded                   //값 타입
    private Zipcode zipcode;
}
