package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
@Getter
@Setter
public class Period {

    protected Period() {
    }
}
