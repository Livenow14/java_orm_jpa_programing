package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class PhoneNumber {

    String areaCode;
    String localNumber;
    @ManyToOne PhoneServiceProvider provider;   //엔티티 참조
}
