package domain;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class Zipcode {
    String zip;
    String plusFour;
}
